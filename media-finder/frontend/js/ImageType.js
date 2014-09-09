define(["widget", "pubsub", "ControlWidget", "tmpl!ImageType", "SearchInfo"],
function(widget, pubsub, controlWidget, template, searchInfo){
  function ImageType(selector, data){
    this._data = data;
    this._template = template;

    mixin(widget, pubsub, controlWidget, this);

    this.placeInDom(selector);
  }

  ImageType.prototype.loadTypes = function(data){
    this.render(data);
    if(this._types)
      this.setTypes(this._types);
  };

  ImageType.prototype.loadCounts = function(data){
    this.$$('[data-count]').each(function(index, element){
      $(element).text(0).closest('.type').find('input').attr('disabled', 'disabled');
      for(var i in data){
        if($(element).attr('data-count') === data[i].name){
          $(element).text(data[i].count).closest('.type').find('input').removeAttr('disabled');
        }
      }
    });
  };

  ImageType.prototype.setTypes = function(types){
    this._types = types;
    var self = this;
    if(types && types.length > 0){
      self.expand();
      self.$$('.type input[name]').removeAttr('checked');
      types.forEach(function(type){
        self.$$('.type input[name="' + type + '"]').attr('checked', 'checked');
      });
    }
  };

  ImageType.prototype.onTypeSelected = function(callback){
    this.on('typeSelected', callback);
  };

  ImageType.prototype._wireUp = function(elem, $$, self){
    var arrow = $$('.arrow'),
        header = arrow.closest('h3'),
        types = $$('.type input[type="checkbox"]');

    types.click(function(){
      var selectedType = $$('.type input:checked').map(function(){
        return $(this).attr('name');
      }).get();
      self.fire('typeSelected', selectedType);
    });

    header.click(function(){
      if(arrow.hasClass('down')){
        self.collapse();
      }else{
        self.expand();
      }
    });
  };

  return ImageType;
});
