define(["widget", "pubsub","tmpl!CollectionSummary"],
function(widget,   pubsub,  template){
  function CollectionSummary(selector){
    this._nResults = 0;
    this._template = template;
    this._data = undefined;
    this._current = undefined;

    mixin(pubsub, widget, this).placeInDom(selector);
  }

  CollectionSummary.prototype.slideUp = function(){

    this.fire('show', this._current);

    $('#resultsHeader').addClass('disabled'); // makes results-header look less inviting
    var elem = this._elem,
      collectionDrawer = $('#collectionResults #resultsGrid'),
      top, bottom;

    if(!elem.hasClass('up')){
      bottom = $(window).height() - 82 - elem.get(0)['offsetHeight'];
      top = 82 + elem.get(0)['offsetHeight'];
      collectionDrawer.animate({'height': bottom - 18}, 300);
      elem.animate({"bottom" : bottom}, 300);
      setTimeout(function(){
        collectionDrawer.css('top', top);
        collectionDrawer.css('height', 'inherit');
        elem.css("bottom", 'inherit');
        elem.css("top", '82px');
      }, 500);
      elem.addClass('up');
    }
  };

	CollectionSummary.prototype.slideDown = function(){
		$('#resultsHeader').removeClass('disabled'); // makes the results header look enabled
		var elem = this._elem,
			collectionDrawer = $('#collectionResults #resultsGrid'),
			top;

		if(elem.hasClass('up')){
			// $('#collectionResults #resultsGrid').height(0);
			top = $(window).height() - elem.get(0)['offsetHeight'];
			collectionDrawer.animate({'top': $(window).height()}, 300);
			elem.animate({'top': top}, 300);
		}
		setTimeout(function(){
			collectionDrawer.css('top', 'inherit');
			collectionDrawer.css('height', 0);
			elem.css("bottom", 0);
			elem.css("top", 'inherit');
		}, 500);
		elem.removeClass('up');
	};

  CollectionSummary.prototype.slideToggle = function(){
    if(this._elem.hasClass('up')){
      this.slideDown();
    } else {
      this.slideUp();
    }
  };

  CollectionSummary.prototype._wireUp = function(elem, $$, self){
    this._elem = elem;
    elem.on('click', function(){
      self.slideToggle.apply(self);
    });
	self.slideDown(); // initialize state, mostly for the state of hte results grid actually
  };

  CollectionSummary.prototype.setCurrent = function(data, show){
    this._current = data;
    if(show){
      this.slideUp();
    }
    this.$$('.crumbs').text( data.path.join(' > ') );
    this._elem
        .attr('data-id', data.id)
        .attr('data-name', data.name);
  };

  return CollectionSummary;
});
