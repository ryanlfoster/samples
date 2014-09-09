define({
  placeInDom: function(parent){
    if ("_elem" in this && this._elem !== null && this._elem !== undefined){
      $(parent).append(this._elem);
    } else if ("_template" in this && "_data" in this){
      this._elem = $(this._template(this._data));
      $(parent).append(this._elem);
    } else {
      this._elem = $(parent);
    }

    if (typeof this._wireUp === "function" && this.$$.bind){
      this._wireUp(this._elem, this.$$.bind(this), this);
    }else{
      this._wireUp(this._elem, this.$$, this);
    }
  },

  render: function(newData){
    if (newData !== undefined){
      this._data = newData;
    }
    var newElem = $(this._template(this._data));
    this._elem.replaceWith(newElem);
    this._elem = newElem;

    if (typeof this._wireUp === "function" && this.$$.bind){
      this._wireUp(this._elem, this.$$.bind(this), this);
    }else{
      this._wireUp(this._elem, this.$$, this);
    }
  },

  $$: function(selector){
    return $(selector, this._elem);
  },

  hide: function(){ this._elem.hide(); },
  show: function(){ this._elem.show(); },
  toggle: function(){ this._elem.toggle(); }
});
