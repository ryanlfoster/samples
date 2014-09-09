define(["tmpl!Result", "Preview", "Details",
  "Download", "IpInfo", "CollectionsPopup", "CollectionService", "DeletePopup", "widget", "pubsub", "dimensions"],
function(template, Preview, Details, Download, IpInfo, CollectionsPopup, CollectionService, DeletePopup, widget, pubsub, dim){
  var IMAGE_BOX_SIZE = dim.SearchResultWidth - dim.SearchResultPadding -
                       (dim.SearchResultImagePadding * 2);

  var NOT_AVAILABLE_IMAGE = 'img/notAvailable.png';

  function Result(selector, data){
    this._data = data;
    this._template = template;
    this._preview = null;
  this._service = new CollectionService();

    mixin(widget, pubsub, this).placeInDom(selector);
  }

  Result.prototype.fadeUp = function(time){
    this._elem.animate({opacity:1.0}, time);
  };

  Result.prototype.destroy = function(){
    this._elem.remove();
  };

  Result.prototype.onImageLoad = function(callback){
    var img = this.$$(".resultImage");

    img.load(callback);

    //treat error loading the same as finish loading
    img.error(callback);

    //if there is no image call the callback immediately
    //    and set the source to the notAvailable image
    if (!this._data.metadata.thumbnailUrl){
      img.attr("src", NOT_AVAILABLE_IMAGE);
      callback();
    }
  };

  Result.prototype._showPreview = function(){
    var img = this.$$('img.resultImage');
    var aspectRatio = img.width() / img.height();
    this._preview = new Preview(this._data, this._elem.offset(), aspectRatio);
  };

  Result.prototype._hidePreview = function(){
    if (this._preview){
      this._preview.destroy();
      delete this._preview;
      this._preview = null;
    }
  };

  Result.prototype._showDetails = function(){
    new Details(this._data);
  };

  Result.prototype._showDownload = function(offset){
    new Download(this._data, offset);
  };

  Result.prototype._showIpInfo = function(offset){
    var code = this._data.rightsManagement.ipCode;
    offset.left += 12;
    new IpInfo(offset, code);
  };

  Result.prototype._showCollectPopup = function(offset){
    var myId = this._elem.data('entry-id'),
        name = this._elem.data('name');
        data = {
          assetId     : myId,
          name        : name,
          collection  : $('#collectionSummary').attr('data-name'),
          collectionId: $('#collectionSummary').attr('data-id')
        };
    offset.left += 12;
    new CollectionsPopup(offset, data);
  };

  Result.prototype._showDeletePopup = function(offset){
    var myId = this._elem.data('entry-id'),
      name = this._elem.data('asset-name'),
      collName = $('#collectionSummary').attr('data-name');

    if(!name || name === "[object Object]"){
      name = 'this asset from the collection "'+collName+'"';
    } else {
      name='"'+name+'" from the collection "'+collName+'"';
    }

    data = {
      assetId     : myId,
      name        : name,
      collection  : collName,
      collectionId: $('#collectionSummary').attr('data-id'),
      type: 'Asset'
    };
    var self = this;
    offset.left += 12;
    new DeletePopup(offset, data, function(){
      self._service.destroyEntry(data.collectionId, myId, function(){
        self.destroy();
      }, function(){
        console.log('del asset NO WORKY');
      });
    });
  };

  Result.prototype._wireUp = function(elem, $$, self){
    resizeImages(elem);
    var hoverTimeout;
    $$('.imageBox').on('mouseenter', function(event){
      hoverTimeout = setTimeout(function(){
        self._showPreview(); }, 200);
    });

    $$('.imageBox').on('mouseleave', function(event){
      clearTimeout(hoverTimeout);
      self._hidePreview();
    });

    $$('.resultImage').click(function(){
      self._showDetails();
    });

    $$('.ip').click(function(){
      self._showIpInfo($(this).offset());
    });

    $$('.collect').click(function(){
      self._showCollectPopup($(this).offset());
    });

    $$('.delete').click(function(){
      self._showDeletePopup($(this).offset());
    });

    $$('.download').click(function(){
      self._showDownload($(this).offset());
    });
  };

  function resizeImages(elem){
    $('.imageBox', elem).each(function(){
      var imageBox = $(this);
      var img = $('.resultImage', this);
      img.hide();
      var padding = $('.padding', this);
      img.load(function(){
        var width = img.width();
        var height = img.height();
        var scale = IMAGE_BOX_SIZE / Math.max(width, height);
        img.width(width * scale);
        img.height(height * scale);
        if (width > height){
          var boxPlusPadding = IMAGE_BOX_SIZE + 2*dim.SearchResultImagePadding;
          var newHeight = (parseInt(boxPlusPadding, 10) - img.height())/2 + "px";
        }
        img.show();
      });
    });
  }

  return Result;
});
