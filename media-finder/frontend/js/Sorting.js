define(["widget", "pubsub", "tmpl!Sorting"],
function(widget, pubsub, template){
  function Sorting(selector, data){
    this._data = data;
    this._template = template;

    mixin(widget, pubsub, this);

    this.placeInDom(selector);
  }

  Sorting.prototype.setSort = function(sort){
    this.fire("sortChanged", sort);
  };

  Sorting.prototype.onSort = function(callback){
    this.on("onSort", callback);
  };

  Sorting.prototype._wireUp = function(elem, $$, self){
    //Everytime a sort by term is changed
    self.on("sortChanged", function(sort){
      //setting default
      if(sort === null){
        sort = "descending";
      }
      if($$('.option [data-value="' + sort + '"]').length < 1){
        var target = $$('.options [data-value="' + sort + '"]');
        var changed = $$('.option [data-value]');
        $$('.option [data-value]').replaceWith(target);
        target.after(changed);
        $$('.options').prepend(changed).hide();
        var toBeSorted = $$('.options [data-value]');
        if(toBeSorted)
          $$('.options').html(toBeSorted.sort(sortOptions));
      }
    });

    //Mimic select box functionality
    $$('.option').on('click', function(e){
      $$('.options').toggle();
    });

    //handle the clicking of a sort option
    $$('.options').on('click', function(e){
      var sort = $(e.target).attr('data-value');
      self.fire("onSort", sort);
    });
  };

  function sortOptions(a, b){
    var one = $(a).text();
    var two = $(b).text();
    if(one > two){
      return 1;
    }else if (one < two){
      return 0;
    }
    return -1;
  }

  return Sorting;
});
