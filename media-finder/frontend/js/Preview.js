define(["popup", "widget", "tmpl!Preview", "dimensions"],
function(popup, widget ,  template, dimensions){
  function Preview(data, offset){
    this._offset = offset;
    this._data = data;
    this._template = template;

    mixin(popup, widget, this).placeInDom("body");
  }

  Preview.prototype._wireUp = function(elem, $$, self){

    $$(".close").click(function(){
      self.destroy();
    });

    var img = $$(".previewImage");
    img.load(function(){
      elem.css({visibility: "visible"});
      self.popup(self._calculatePopupPosition(self._offset, img));
    });
  };

  Preview.prototype._calculatePopupPosition = function(offset, img){
    //todo: pull out the constants into a master dimentions class
    var resultWidth   = parseInt(dimensions.SearchResultWidth, 10);
    var resultPadding = parseInt(dimensions.SearchResultPadding, 10);
    var previewImageBoxSize = 512;
    var previewPadding = 20;
    var previewTextHeight = 120;

    var modalPadding = 20;
    var gridOffsetRight = resultPadding / 2;
    var gridOffsetLeft = previewPadding * 2 + gridOffsetRight;

    var win = $(window);
    var winHeight = win.height();
    var winWidth = win.width();
    var imgWidth = img.width();
    var imgHeight = img.height();
    var scale;
    offset.top -= win.scrollTop() + 1;

    if (imgWidth > previewImageBoxSize){
      scaleImage(previewImageBoxSize / imgWidth);
    }

    if (imgHeight > previewImageBoxSize){
      scaleImage(previewImageBoxSize / imgHeight);
    }

    var rightOverlap = -(winWidth - (offset.left + resultWidth + imgWidth + gridOffsetLeft));
    var leftOverlap;

    if (rightOverlap > 0){
      leftOverlap = -(offset.left - imgWidth - gridOffsetLeft);
      if (leftOverlap > 0){
        if (leftOverlap < rightOverlap){
          scaleImage(1 - (leftOverlap / (imgWidth - gridOffsetLeft)));
          offset.left -= imgWidth + gridOffsetLeft;
        } else {
          scaleImage(1 - (rightOverlap / (imgWidth + gridOffsetLeft)));
          offset.left += resultWidth - gridOffsetRight;
        }
      } else {
        offset.left -= imgWidth + gridOffsetLeft;
      }
    } else {
      offset.left += resultWidth - gridOffsetRight;
    }

    //scootch it up if needed
    var amount = offset.top + imgHeight + previewTextHeight - winHeight;
    if (amount > 0){
      offset.top -= amount + modalPadding;
    }

    //finally, scale horizontally
    var modalExtraHeight = previewTextHeight + 3 * previewPadding + modalPadding;
    if (offset.top < 0){
      offset.top = 0;
      if (imgHeight + modalExtraHeight > winHeight){
        scaleImage(winHeight / (imgHeight + modalExtraHeight));
      }
    }

    this._elem.width(imgWidth);

    return offset;

    function scaleImage(scale){
      imgWidth *= scale;
      img.width(imgWidth);
      imgHeight *= scale;
      img.height(imgHeight);
    }
  };

  return Preview;
});
