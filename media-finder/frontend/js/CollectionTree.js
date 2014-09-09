define(["widget", "pubsub", 'CollectionService', 'DeletePopup'],
function(widget,   pubsub,   CollectionService,   DeletePopup){

	function CollectionTree(data, selector, controller){
		this._template = function(){ return '<div class="collectionTree"></div>'; };
		this._data = data || [{data:'loading',children:[{data:'...',id:123}]},{data:'please wait'}]; // this test
		this._service = new CollectionService();
		this._controller = controller;

		mixin(pubsub, widget, this).placeInDom(selector);
	}

	// this is just a starting stub for the Collection Tree stuff
	// just a copy from another file im using for reference
	CollectionTree.prototype.create = function(updateTree){
		this._inst().create("li:contains('My Collections'):first", "first", {data: "New Collection", attr: {'data-new': 'true', 'rel': 'ownprivate'}}, function(obj){obj.removeAttr('data-new');}); // the space _New is intentional
	};

	CollectionTree.prototype.destroy = function(){
		var self = this;
		var current = this.getSelected();
		if(!current.attr('data-favorite') && current.attr('data-ownership') === "OWNER"){

			var data = {name : '"'+current.attr('data-name')+'"', type:'Collection'},
				offset = current.find('a').offset(),
				collOff = $('#collections').outerWidth();

			offset.left += current.find('a').outerWidth();
			if (offset.left>collOff){
				offset.left=collOff;
			}

			new DeletePopup(offset, data, function(){
				self._inst().remove();
			});
		}
	};

	CollectionTree.prototype.rename = function(){
		var current = this.getSelected();
		if(!current.attr('data-favorite') && current.attr('data-ownership') === "OWNER"){
			this._inst().rename();
		}
	};

	CollectionTree.prototype.getSelected = function(){
		return this._inst().get_selected();
	};

	CollectionTree.prototype._wireUp = function (elem, $$, self) {
		var test = _.find(self._data[0].children, function(col){
			return col.data == "Favorites";
		});
		if(!test){ // shouldnt ever happen, but just in case…
			test = self._data[0].children[0];
		}
			// jstree places a UL inside the `elem`
		$(elem).jstree({
			core:{/*core options here*/},
			themes: {
				"theme" : "default",
				"dots" : false,
				"icons" : true
			},
			dnd:{
				'drag_finish':function(data){
					var entryId = $(data.o).closest('.jstree-draggable').attr('data-entry-id'),
						targetId = $(data.r).attr('id');
					// console.log('just tried to drag foreign '+entryId+' into tree node '+ targetId );
					self._service.addEntry(targetId, entryId, function(){console.log('Yay!')}, function(){console.log('drats :-(')});
				}
			},
			crrm:{
				move: {
					"check_move" : function(m){

						// np = new parent
						// op = old parent
						// o = origin node, or node being moved

						// NO moving collectoin into to same parent
						// dont want it to look like you can reorder nodes
						if(m.np[0].id === m.op[0].id){
							//console.log(11);
							return false;
						}

						// dont move collections into FAVORITE collection
						if($(m.np[0]).data('favorite')===true) {
							//console.log(12);
							return false;
						}

						// NO moving the Favorites collection
						if($(m.o[0]).data('favorite')===true) {
							//console.log(13);
							return false;
						}

						// NO MOVING colelctions into shared / unowned collections
						if($(m.np[0]).attr('rel')==='contribroot')  {
							//console.log(14);
							return false;
						}

						// NO moving shared collections
						if($(m.o[0]).attr('rel')==='contribroot' || $(m.o[0]).attr('rel')==='contribshared')  {
							//console.log(15);
							return false;
						}

						// ALLOW move into My Collections ROOT level node
						if($(m.np[0]).attr('rel')==='ownroot' )  {
							//console.log(16);
							return true;
						}

						// DO allow moving collections into other collections (nodes with an ID)
						// dont move into FAVORITE collection
						if(m.np[0].id) {
							//console.log(17);
							return true;
						}

						// if we didnt account for it then we shouldnt be trying to move there
						//console.log(18);
						return false;
					}
				}
			},
			ui: {
				"select_limit": "1",
				"initially_select" : [ test.id.toString() ]
			},
			types: {
				valid_children:['ownroot','contribroot'],
				types: {
					ownroot:{
						icon:{ image:'/img/icons/blue-folder-horizontal.png' },
						valid_children:['ownprivate','ownshared'],
						max_depth:6
					},
					ownprivate:{
						icon:{ image:'/img/icons/blue-folder-horizontal.png' },
						valid_children:['ownprivate','ownshared']
					},
					ownshared:{
						icon:{ image:'/img/icons/blue-folder-share.png' },
						valid_children:['ownprivate','ownshared']
					},
					contribroot:{
						icon:{ image:'/img/icons/blue-folder-share.png' },
						valid_children:['contribshared'],
						max_depth:6
					},
					contribshared:{
						icon:{ image:'/img/icons/blue-folder-share.png' },
						valid_children:['contribshared']
					}
				}
			},
			plugins:['json_data', 'ui', 'crrm', 'themes', 'sort', 'dnd', 'types'],
			json_data:{
				"data": self._data
			},
			sort : function (a, b) {// This is done to make sure that the default "Favorites" collection is always on top
				if ($(b).attr('data-favorite')) {
					return 1;
				} else if ($(a).attr('data-favorite')) {
					return -1;
				}
				if ($(b).attr('data-new')) {
					return 1;
				} else if ($(a).attr('data-new')) {
					return -1;
				}
				return this.get_text(a) > this.get_text(b) ? 1 : -1;
			}


		}).bind('move_node.jstree', function(e, data){
				var child = data.rslt.o.attr('id'),
					newParent = data.rslt.cr == -1? undefined: data.rslt.np.attr('id'),
					oldParent = data.rslt.op.attr('id');

				//console.log("child="+child,
				//	"newParent="+newParent,
				//	"oldParent="+oldParent);

				// the callback for successfull tansactions
				var allDone = function(result){
					console.log(self._getSelected());
					self.fire('changecurrent', self._getSelected(), false);
				};

				if(oldParent && !newParent){ // Moved from some parent to ROOT
					//console.log(child + ' was pulled out of ' + oldParent);
					self._service.destroyEntry(oldParent, child, allDone, _bldErr(data.rlbk));

				} else if(oldParent && newParent){ // Moved from some parent to ANOTHER parent
					//console.log(child + ' was pulled out of ' + oldParent);
					//console.log(child + ' was dropped inside of ' + newParent);
					self._service.destroyEntry(oldParent, child, function(){
						self._service.addEntry(newParent, child, allDone, _bldErr(data.rlbk));
					}, _bldErr(data.rlbk));

				} else if (!oldParent && newParent) { // Moved from ROOT to some parent
					//console.log(child + ' was dropped inside of ' + newParent);
					self._service.addEntry(newParent, child, allDone, _bldErr(data.rlbk));

				} else { // some unaccounted for pattern
					// in the case that the above conditions aren't as reliable as we once thought
					//console.log('that moving pattern is not supported and is probably an error on our part, sorry :-(');
					$.jstree.rollback(data.rlbk);
				}

		}).bind('create.jstree', function(e, data){
			self._service.create(data.rslt.name, function(result){
				data.rslt.obj
					.attr('id', result.id)
					.attr('data-name', data.rslt.name)
					.attr('data-ownership', 'OWNER') //Since they created it we can assume they are the owners
					.find('a').click();
				self._inst().set_text('#' + result.id, data.rslt.name);
				self.fire('collectionCreated', result);
			}, _bldErr(data.rlbk));

		}).bind('remove.jstree', function(e, data){
			var deleted = data.rslt.obj;
			self._service.destroy(deleted.attr('id'), function(result){
				deleted.find('ul li').each(function(){
					self._service.destroy($(this).attr('id'), function(result){}, _bldErr(data.rlbk));
				});
			}, _bldErr(data.rlbk));

		}).bind('rename.jstree', function(e, data){
			self._service.update(data.rslt.obj.attr('id'), data.rslt.new_name, function(result){
				self.fire('changecurrent', self._getSelected(), false);
			}, _bldErr(data.rlbk));

		}).bind('select_node.jstree', function (e, data) {
			var c = $(data.rslt.obj),
				collEntry = self._getSelected();

			// open the node to display its children
			self._inst().open_node(c);
			if(c.attr('id')) {
				if($('#collections').is(':visible') && data.args.length >= 3){
					self.fire('changecurrent', collEntry, true);
				}else{
					self.fire('changecurrent', collEntry, false);
				}
			} else {
				data.inst.deselect_node(c);
			}
			if(!c.attr('data-favorite') && c.attr('data-ownership') === "OWNER"){
				if(self._controller) self._controller.enableActions();
			}else{
				if(self._controller) self._controller.disableActions('rename', 'delete', 'share');
			}

		}).bind('loaded.jstree',function(e, data){
			self.updateSharingIcons();
		});
	};

	CollectionTree.prototype.updateSharingIcons = function(){
		var treeInstance = $(this._elem);
		var self = this;
		treeInstance.find('.jstree-leaf[rel="ownprivate"], .jstree-leaf[rel="ownshared"]').each(function(i,e){
			window.setTimeout(function(){   //    // as this data isnt SUPER time-sensitive we will TRULY async this
				self._service.getUsers(e.id, function(data){
					if(data.user && data.user instanceof Array && data.user.length>1){
						self._inst().set_type('ownshared','#'+ e.id);
					} else {
						self._inst().set_type('ownprivate','#'+ e.id);
					}
				},function(){ /*nothing to do on error*/ });
			},0);

		});

	};

	CollectionTree.prototype._getSelected = function(){

		var c = this._inst().get_selected();
		return {
			id:		c.attr('id'),
			name:   this._inst().get_text(),
			path:	this._inst().get_path()
		};
	};

	CollectionTree.prototype.getData = function(){
		return this._inst().get_json(-1, ['id','rel','data-name','data-ownership','data-favorite','class']);
	};

	CollectionTree.prototype.setData = function(data){
		this._inst()._get_settings().json_data.data=data;
		this._inst().refresh(-1);
	};

	CollectionTree.prototype._inst = function(){
		return $.jstree._reference(this._elem);
	};

	function _bldErr(rlbk) {
		return function(result){
			$.jstree.rollback(rlbk);
		};
	}

	return CollectionTree;
});
