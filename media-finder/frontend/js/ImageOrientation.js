define(["widget", "pubsub", "ControlWidget", "tmpl!ImageOrientation", "SearchInfo"],
function(widget, pubsub, controlWidget, template, searchInfo){
  function ImageOrientation(selector, data){
    this._data = data;
    this._template = template;

    mixin(widget, pubsub, controlWidget, this);

    this.placeInDom(selector);
  }

  ImageOrientation.prototype.loadOrientation = function(data){
    this.render(data);
    if(this._orientations)
      this.setOrientation(this._orientations);
  };

  ImageOrientation.prototype.loadCounts = function(data){
    this.$$('[data-count]').each(function(index, element){
      $(element).text(0).closest('.orientation').find('input').attr('disabled', 'disabled');
      for(var i in data){
        if($(element).attr('data-count') === data[i].name){
          $(element).text(data[i].count).closest('.orientation').find('input').removeAttr('disabled');
        }
      }
    });
  };

  ImageOrientation.prototype.setOrientation = function(orientations){
    this._orientations = orientations;
    var self = this;
    if(orientations && orientations.length > 0){
      this.expand();
      self.$$('.orientation input[name]').removeAttr('checked');
      orientations.forEach(function(orientation){
        self.$$('.orientation input[name="' + orientation + '"]').attr('checked', 'checked');
      });
    }
  };

  ImageOrientation.prototype.onOrientationSelected = function(callback){
    this.on('orientationSelected', callback);
  };

  ImageOrientation.prototype._wireUp = function(elem, $$, self){
    var arrow = $$('.arrow'),
        header = arrow.closest('h3'),
        types = $$('.orientation input[type="checkbox"]');

    types.click(function(){
      var selectedType = $$('.orientation input:checked').map(function(){
        return $(this).attr('name');
      }).get();
      self.fire('orientationSelected', selectedType);
    });

    header.click(function(){
      if(arrow.hasClass('down')){
        self.collapse();
      }else{
        self.expand();
      }
    });
  };

  return ImageOrientation;
});
