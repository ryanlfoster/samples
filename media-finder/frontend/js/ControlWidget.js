define({
  expand: function(){
    $('.arrow', this._elem).addClass('down');
    $('.collapsable', this._elem).slideDown();
  },
  collapse: function(arrow, collapsable){
    $('.arrow', this._elem).removeClass('down');
    $('.collapsable', this._elem).slideUp();
  }
});
