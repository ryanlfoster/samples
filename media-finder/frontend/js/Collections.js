define(["widget", "pubsub", "CollectionService", "CollectionTree", "CollectionShared", "tmpl!Collections"],
	function(widget,   pubsub, CollectionService, CollectionTree, CollectionShared, template){

	function Collections(selector){
		this._template = template;
		this._data = undefined;
		this._service = new CollectionService();
		this._myTree = {};

		mixin(pubsub, widget, this).placeInDom(selector);

		//Just an idea to handle App wide event PubSub
		window.collections = {};
		var self = this;
		window.collections.setMainCollData = function(data){
			self._myTree.setData(data);
		};
		window.collections.getMainCollData = function(data){
			return self._myTree.getData();
		};
	}

	Collections.prototype.newCollectionData = function(data){
		$('.allCollectionsTree').empty();
		this._myTree = new CollectionTree(data, '.allCollectionsTree', this);

		// here is a little wonky 'cause its a pubsub passthrough
		// then the trees 'select' event is fired we want to fire our own 'change' event
		var self = this; // i need 'self' cause 'this' gets changed-up
		this._myTree.on('changecurrent',function(x, y){
			self.fire('changecurrent',x, y);
		});

	};

	Collections.prototype.disableActions = function(){
		if(arguments.length > 0){
			for(var i = 0; i <= arguments.length; i++){
				this.$$('[data-action="' + arguments[i] + '"]').addClass('disabled');
			}
		}else{
			this.$$('[data-action]').addClass('disabled');
		}
	};

	Collections.prototype.enableActions = function(){
		if(arguments.length > 0){
			for(var i in arguments){
				this.$$('[data-action="' + arguments[i] + '"]').removeClass('disabled');
			}
		}else{
			this.$$('[data-action]').removeClass('disabled');
		}
	};

	Collections.prototype.showSharePanel = function(){
		var currentCollection = this._myTree.getSelected();
		var self = this;
		if(!currentCollection.attr('data-favorite') && currentCollection.attr('data-ownership') === "OWNER"){
			var	data = {},
				collId = currentCollection.attr('id'),
				collName = currentCollection.attr('data-name');
			data.collectionId = collId;
			data.collectionName = collName;
			data.user = [];
			var shared = new CollectionShared(data, '#collections');
			this.disableActions();
			shared.on('close', function(){self.enableActions();});
			shared.on('close', function(){self._myTree.updateSharingIcons();});
			this._service.getUsers(collId, function(data){
				data.collectionId = collId;
				data.collectionName = collName;
				data.user = data.user instanceof Array ? data.user : [data.user];
				shared.addUsers(data);
			}, function(){ /* no error handling? */ });
		}
	};

  Collections.prototype._wireUp = function(elem, $$, self){
		// the KEY in this map matches the ID of the elements used to control visibility
		self._service.getAll(function(data){
			self.newCollectionData(data);
		});

		$(elem).on('click', '[data-action="new"]', function(e){
			if(!$(this).hasClass('disabled')){
				self._myTree.create();
			}
		});

		$(elem).on('click', '[data-action="delete"]', function(e){
			if(!$(this).hasClass('disabled')){
				self._myTree.destroy();
			}
		});

		$(elem).on('click', '[data-action="rename"]', function(e){
			if(!$(this).hasClass('disabled')){
				self._myTree.rename();
			}
		});

		$(elem).on('click', '[data-action="share"]', function(e){
			if(!$(this).hasClass('disabled')){
				self.showSharePanel();
			}
		});

		$(elem).on('click', '[data-collection-id]', function(){
			$('.crumbs').text($(this).text()).attr('data-collection-id', $(this).attr('data-collection-id'));
		});
	};

	return Collections;
});
