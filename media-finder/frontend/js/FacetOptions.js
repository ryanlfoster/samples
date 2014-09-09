define(["widget", "pubsub", "ControlWidget", "tmpl!FacetOptions", "omniture"],
function(widget,   pubsub,  controlWidget,  template,            omniture){
  var KEYWORDS_PER_RENDER = 10;
  var KEYWORD_HEIGHT = 26;
  var KEYWORDS_IN_WINDOW = 6;

  function FacetOptions(selector, data){
    this._allKeywords = [];
    this._currentKeywords = [];
    this._filteredKeywords = [];

    this._template = template;
    this._data = data;

    this._renderPoint = 0;
    this._renderTimes = 0;

    mixin(widget, pubsub, controlWidget, this);

    this.placeInDom(selector);
    this.hide();
  }

  FacetOptions.prototype.onKeywordSelected = function(callback){
    this.on("keywordSelected", callback);
  };

  FacetOptions.prototype.setCurrentKeywords = function(keywords){
    this._currentKeywords = keywords;
  };

  FacetOptions.prototype.setKeywords = function(data){
    var self = this;
    this._allKeywords = (findKeywords(data) || {})["facet-value"] || [];

    //make sure it's an array
    if (!(this._allKeywords instanceof Array)){
      this._allKeywords = [this._allKeywords];
    }

    //some keywords are empty objects!
    this._allKeywords = this._allKeywords.filter(function(keyword){
      return (typeof keyword === "object" && typeof keyword.name === "string");
    });


    alphabetize(this._allKeywords);

    this._filteredKeywords = this._allKeywords = this._allKeywords.filter(function(keyword){
      return (self._currentKeywords.indexOf(keyword.name) == -1);
    });
    this._showKeywords();
    this._checkHeight();
  };

  FacetOptions.prototype._showKeywords = function(){
    this.render({keywords: this._filteredKeywords.slice(0, KEYWORDS_PER_RENDER)});
    this._renderPoint = KEYWORDS_PER_RENDER;
    this._renderTimes = 1;
  };

  FacetOptions.prototype._showMoreKeywords = function(){
    //use the same template, but then scrape the important data out for use
    var start = this._renderPoint;
    this._renderPoint += KEYWORDS_PER_RENDER;
    var items = this._filteredKeywords.slice(start, this._renderPoint);
    var fragment = $(template({keywords: items}));
    var content = $(".keywordsList>.keyword", fragment);
    this._wireUpKeywords(content);
    var insertPoint = this.$$(".keywordsList");
    content.appendTo(insertPoint);
    this._renderTimes += 1;
  };

  FacetOptions.prototype._checkHeight = function(){
    var items = Math.min(6, this._filteredKeywords.length);
    var height = Math.max(2*KEYWORD_HEIGHT, items * KEYWORD_HEIGHT);

    this.$$(".keywordsList")[items === 0 ? "addClass" : "removeClass"]("noneLeft");
    this.$$(".keywordsList").height(height);
  };

  FacetOptions.prototype._filterKeywords = function(str){
    try {
      var regex = new RegExp(str, 'i');
      this._filteredKeywords = this._allKeywords.filter(function(keyword){
        return !!keyword.name.match(regex);
      });
      this._renderPoint = 0;
      this._renderTimes = 0;
      this._clearFacetsOut();
      this._showMoreKeywords();
      this._checkHeight();
    } catch (e) {/*invalid regex, skip it*/}
  };

  FacetOptions.prototype._clearFacetsOut = function(){
    this.$$(".keyword").remove();
  };

  FacetOptions.prototype._wireUpKeywords = function(keywords){
    var self = this;
    keywords.click(function(){
      self.fire("keywordSelected", $(this).attr("data-name"));
    });
  };

  FacetOptions.prototype._wireUp = function(elem, $$, self){
    this._wireUpKeywords( $$(".keyword"));

    var list = $$(".keywordsList"),
        filter = $$(".filterInput"),
        icon = $$(".icon"),
        arrow = $$('.arrow'),
        header = arrow.closest('h3');

    list.scroll(function(){
      var top = list.scrollTop() + (KEYWORDS_IN_WINDOW * (KEYWORD_HEIGHT+5));
      var height = self._renderTimes * KEYWORDS_PER_RENDER * KEYWORD_HEIGHT;
      if (top >= height){
        self._showMoreKeywords();
      }
    });

    header.click(function(){
      if(arrow.hasClass('down')){
        self.collapse();
      }else{
        self.expand();
      }
    });

    filter.keyup(function(){
      var filterBy = filter.val();
      self._filterKeywords(filterBy);
      icon[filterBy.length ? "addClass" : "removeClass"]("hasInput");
    });

    icon.click(function(){
      filter.val("");
      self._filterKeywords("");
      icon.removeClass("hasInput");
    });

  };

  function findKeywords(data){
    return data.facets.filter(function(facet){
      return facet["facet-type"] === "keyword";
    })[0];
  }

  //todo: consider doing this off in a webworker
  function alphabetize(keywords){
    //sort by the name of the keyword, but push all of the
    //   keywords that stat with a numberto the end
    keywords.sort(function(k1, k2){
      var k1IsAlpha = /[a-z]/i.test(k1.name[0]);
      var k2IsAlpha = /[a-z]/i.test(k2.name[0]);

      if (k1IsAlpha != k2IsAlpha){
        return k1IsAlpha ? -1 : 1;
      }

      if (k1.name > k2.name){
        return 1;
      } else if (k1.name < k2.name){
        return -1;
      } else {
        return 0;
      }
    });
  }

  return FacetOptions;
});
