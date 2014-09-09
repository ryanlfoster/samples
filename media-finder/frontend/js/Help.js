define(["widget", "pubsub", "tmpl!Help"],
function(widget,   pubsub,   template){

	function Help(selector){
		this._template = template;
		this._data = undefined;
		mixin(widget, pubsub, this).placeInDom(selector);
	}

	Help.prototype._wireUp = function(elem, $$, self){
		$$('.action').on('click', function(){
			$$('.dropdown').toggle();
		});

		$(document).on('click', function(e){
			if($(e.target).closest('#help').length === 0){
				$$('.dropdown').hide();
			}
		});
	};

	return Help;
});
