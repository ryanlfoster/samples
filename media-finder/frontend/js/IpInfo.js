define(["popup", "widget",  "tmpl!IpInfo", "tmpl!IpCodeInfo"],
function(popup,   widget,   allInfo, someInfo){
  function IpInfo(offset, code){

    var forOne = code !== undefined;

    this._template = forOne ? someInfo : allInfo ;

    this._data = {code:code};

    mixin(widget, popup, this);

    this.placeInDom("body");

    offset.top -= 40;
    offset.left += 10;
    this._position(offset, forOne)
    this.popup(offset, !forOne);
  }

  IpInfo.prototype.showInfoForCategory = function(category){
    var show = pref.indexOf(category) === -1;

    if (show){
      this.popup(this.offset, true);
      this._elem.addClass(category+"Info");
    } else {
      this.destroy();
    }
  }

  IpInfo.prototype._position = function(offset, forOne){
    var width = this._elem.width();
    if (forOne){
      offset.top -= $(document).scrollTop();
    }
    if (offset.left + width > $(window).width()){
      offset.left -= width + 30;
      this.$$(".arrow").addClass("right");
    }
  }

  IpInfo.prototype._wireUp = function(elem, $$, self){
    $$(".close, .arrow").click(function(){
      self.destroy();
    });
  }

  return IpInfo;
});
