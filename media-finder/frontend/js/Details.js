define(["popup", "widget", "Download", "tmpl!Details", "omniture"],
function(popup, widget, Download, template, omniture){
  var PREVIEW_FADEIN_TIME = 300; //miliseconds
  var FULLSIZE_FADEIN_TIME = 200; //miliseconds

  function Details(data){
    mixin(popup, widget, this);

      this._data = data;
      this._template = template;
      this._download = null;
      this.placeInDom("body");
      this.popupModal();

    omniture.reportDetailOpened(data.id || "<ID UNKNOWN>");
  }

  Details.prototype._wireUp = function(elem, $$, self){
    
    centerImage(elem);

    //if the download window is open, close it as well
    self.onDestroy(function(){
      if (self._download){ self._download.destroy(); }
    });

    $$(".close, .keyword").click(function(){
      self.destroy();
    });

    $$(".sectionHeading, .accordion-arrow").click(function(){
      $(this).parent().toggleClass("open").toggleClass("closed");
    });

    //open up the download dialog
    $$(".download").click(function(){
      if (self._download === null){
        self._download = new Download(self._data, $(this).offset(), true);
      } else {
        self._download.destroy();
        self._download = null;
      }
    });
  };

  function centerImage(elem){
    var img = $(".previewImage", elem);
    img.fadeIn(300);
    var box = $(".imageBox", elem);
    img.load(function(){
      var maxWidth = parseInt(img.css("max-width"), 10);
      var maxHeight = parseInt(img.css("max-height"), 10);
      img.css({
        "margin-left": Math.abs(maxWidth  - img.width())  / 2,
        "margin-top":  Math.abs(maxHeight - img.height()) / 2
      });
    });
  }

  Handlebars.registerHelper('toLowerCase', function(value) {
    return new Handlebars.SafeString(value.toLowerCase());
  });

  return Details;
});
