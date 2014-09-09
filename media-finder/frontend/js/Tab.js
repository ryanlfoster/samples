define(["tmpl!Tab", "widget", "pubsub"],
	function(template, widget, pubsub){

		function Tab(selector, data){
			this._data = data;
			this._template = template;

			mixin(widget, pubsub, this).placeInDom(selector);
		}

		Tab.prototype.selected = function(){
			$('.tab, .tabbed').removeClass('selected');
			this._elem.addClass('selected');
			$(this._data.activate).addClass('selected');
			this.fire('selected');
		};

		Tab.prototype._wireUp = function(elem, $$, self){
			elem.on('click', function(){
				self.selected();
			});
		};

		return Tab;
	});
