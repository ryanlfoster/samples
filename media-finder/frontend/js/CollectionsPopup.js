define(["popup", "widget", "CollectionService", "CollectionTree", "tmpl!CollectionsPopup"],
function(popup,   widget,   CollectionService,   CollectionTree,   tmpl){
  function CollectionsPopup(offset, data){

    this._assetId = data.assetId;
    this._template = tmpl ;
    this._data = data;
    this._myTree = {};
    this._service = new CollectionService();

    mixin(widget, popup, this);

    this.placeInDom("body");

    offset.top -= 40;
    offset.left += 10;
    this._offset = offset;
    this._position(offset);
    this.popup(offset, false);
  }

  CollectionsPopup.prototype._position = function(offset){
    var width = this._elem.width();
    var height = this._elem.height();
    var arrowTop = offset.top + 35;
    offset.top -= $(document).scrollTop();
    if (offset.left + width > $(window).width()){
      offset.left -= width + 30;
      this.$$(".arrow").addClass("right");
    }
    if(offset.top + height > $(window).height()){
      var newOffsetTop = offset.top + height - $(window).height();
      offset.top -= newOffsetTop + 30;
      var self = this;
      setTimeout(function(){
        self.$$(".arrow").offset({top: arrowTop});
      }, 50);
    }
  };

  CollectionsPopup.prototype._wireUp = function(elem, $$, self){
    $$(".close, .arrow").click(function(){
      self.destroy();
    });

    elem.on('click', '[data-action="new"]:not(.disabled)', function(e){
        self._myTree.create();
        $$('[data-action]').addClass('disabled');
        self._myTree.on('collectionCreated', function(coll){
          $$('[data-action]').removeClass('disabled');
        });
    });

    elem.on('click', '[data-action="add"]:not(.disabled)', function(e){
        var collectionId = self._data.collectionId;
        self._service.addEntry(collectionId, self._data.assetId, function(){
          elem.removeClass('current all').addClass('success');
          setTimeout(function(){
            self.destroy();
          }, 3000);
        }, function(){
          elem.removeClass('current all').addClass('error');
        });
    });

    elem.on('click', '[data-action="cancel"]', function(e){
        self.destroy();
    });

    elem.on('click', '[data-action="change"]:not(.disabled)', function(e){
      elem.removeClass('current').addClass('all');

      self._myTree = new CollectionTree(null, '.collectionTree');
      self._myTree.setData(window.collections.getMainCollData());
      self._myTree.on('changecurrent', function(coll){
        self._data.collection = coll.name;
        self._data.collectionId = coll.id;
        $$('[data-action]').removeClass('disabled');
      });
      setTimeout(function(){
        self._position(self._offset);
        self.popup(self._offset, false);
      }, 0);
      self.onDestroy(function(){
        window.collections.setMainCollData(self._myTree.getData());
      });

    });

  };

  return CollectionsPopup;
});
