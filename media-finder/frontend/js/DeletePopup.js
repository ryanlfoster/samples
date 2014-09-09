define(["popup", "widget",  "tmpl!DeletePopup", "tmpl!IpCodeInfo"],
function(popup,   widget,   template, someInfo){
  function DeletePopup(offset, data, successClbk){
    this._template = template;
    this._data = data;
    this._onSuccess = successClbk;
    mixin(widget, popup, this);

    this.placeInDom("body");
	offset.left += 10;
    offset.top -= 40;

    this._position(offset);
    this.popup(offset, false);
  }

  DeletePopup.prototype.showInfoForCategory = function(category){
    var show = pref.indexOf(category) === -1;

    if (show){
      this.popup(this.offset, true);
      this._elem.addClass(category+"Info");
    } else {
      this.destroy();
    }
  };

  DeletePopup.prototype._position = function(offset){
    var width = this._elem.width();
    offset.top -= $(document).scrollTop();
    if (offset.left + width > $(window).width()){
      offset.left -= width + 30;
      this.$$(".arrow").addClass("right");
    }
  };

  DeletePopup.prototype._wireUp = function(elem, $$, self){
    $$(".close, .arrow").click(function(){
      self.destroy();
    });

    $$('.no').on('click', function(){
      self.destroy();
    });

    $$('.yes').on('click', function(){
      self._onSuccess();
      self.destroy();
    });
  };

  return DeletePopup;
});
