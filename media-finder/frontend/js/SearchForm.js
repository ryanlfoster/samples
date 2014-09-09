define(["widget", "pubsub", "tmpl!SearchForm"],
function(widget, pubsub, tmpl){
  function SearchForm(selector){
    this._template = tmpl;
    this._data = {
      imageNumber: Math.floor(Math.random() * 13)
    };
    mixin(pubsub, widget, this).placeInDom(selector);
  }

  SearchForm.prototype.onSearch = function(callback){
    this.on("search", callback);
  };

  SearchForm.prototype.landingPage = function(){
    this.$$("#searchTerm").val("").focus();
    this._elem.addClass("landingPage").removeClass("embedded");
  };

  SearchForm.prototype.embedded = function(term){
    this._elem.removeClass("landingPage");
    var self = this;
    setTimeout(function(){
      self._elem.addClass("embedded");
    }, 1000);
    if (term !== undefined){
      this._setTerm(term);
    }
  };

  SearchForm.prototype._setTerm = function(term){
    this.$$("#searchTerm").val(term);
  };

  SearchForm.prototype._wireUp = function(elem, $$, self){
    $$('#searchTerm').focus();
    $$('#startSearch').click(search);

    //search on enter key
    elem.keyup(function(e){
      //search on enter
      if ((e.code || e.which) == 13){ search(); }
    });

    function search(){
      var term = $$("#searchTerm").val();
      if (term.length > 0){
        self.fire("search", term);
        self.embedded();
      }
    }
  };

  return SearchForm;
});
