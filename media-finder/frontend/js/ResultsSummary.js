define(["widget", "pubsub","tmpl!ResultsSummary"],
function(widget,   pubsub,  template){
  function ResultsSummary(selector){
    this._nResults = 0;
    this._query = "";
    this._keywords = [];
    this._loading = false;
    this._template = template;
    this._data = undefined;

    mixin(pubsub, widget, this).placeInDom(selector);
  }

  ResultsSummary.prototype.error = function(err){
    this._error = ERROR_MESSAGES[parseInt(err, 10)];
    this._redraw(false);
  };

  ResultsSummary.prototype.loading = function(query){
    this._query = query;
    this._redraw(true);
  };

  ResultsSummary.prototype.onKeywordRemoved = function(callback){
    this.on("keywordRemoved", callback);
  };

  ResultsSummary.prototype.setNumberResults = function(number){
    this._nResults = number;
    this._redraw(false);
  };

  ResultsSummary.prototype.setQuery = function(query){
    this._query = query;
  };

  ResultsSummary.prototype.setKeywords = function(keywords){
    this._keywords = keywords;
  };

  ResultsSummary.prototype._redraw = function(reloading){
    this.show();
    this.$$('#pagination').css('display', 'inline-block');
    var data = {
      nResults: this._nResults,
      searchTerm: this._query,
      keywords: this._keywords.length ? this._keywords : undefined,
      reloading: reloading,
      error: this._error
    };
    data.pages = Math.ceil(this._nResults/100);
    data.currentPage = data.pages === 0 ? 0 : 1;
    this._data = data;
    this.render(data);
	this.updatePage(); // quick update since some logice happened a few lines back
  };

	ResultsSummary.prototype.onPageChanged = function(callback) {
		this.on('pageChanged', callback);
	};

	ResultsSummary.prototype._wireUp = function(elem, $$, self){
		$$(".keyword").click(function(){
			var word = $(this).attr("data-name");
			self._removeKeyword(word);
		});

		$('#pagination .back').closest('.buttony').on('click', function(){
			if($(this).closest('.disabled').length > 0) return;
      if(self._data.currentPage > 1){
				self.fire('back');
				self._data.currentPage -= 1;
				self.fire('pageChanged', self._data.currentPage);
				self.updatePage();
			}
		});
		$('#pagination .forward').closest('.buttony').on('click', function(){
			if($(this).closest('.disabled').length > 0) return;
      if(self._data.currentPage < self._data.pages){
				self.fire('forward');
				self._data.currentPage += 1;
				self.fire('pageChanged', self._data.currentPage);
				self.updatePage();
			}
		});

		$('.currentPage').on('keyup', function(e){
			if(e.keyCode === 13){
				pageEntered(e);
			}
		});

    $('.currentPage').on('blur', pageEntered);

    function pageEntered(e){
      var page = parseInt($(e.target).val(), 10);
      if(!isNaN(page) && page > 0 && page <= self._data.pages && page != self._data.currentPage){
        self._data.currentPage = page;
        self.fire('pageChanged', self._data.currentPage);
      }else{
        $(e.target).val(self._data.currentPage);
      }
    }

    elem.width($(window).width() - 290);
    $(window).resize(function(){
      elem.width($(window).width() - 290);
    });
  };

	ResultsSummary.prototype.updatePage = function(){
		var cpage = this._data.currentPage,
			pages = this._data.pages;
		this.$$('.currentPage').val(cpage);
		if (cpage >= pages) {
			this.$$('.forward').closest('.buttony').addClass('disabled');
		} else {
			this.$$('.forward').closest('.buttony').removeClass('disabled');
		}
		if(cpage <= 1){
			this.$$('.back').closest('.buttony').addClass('disabled');
		} else {
			this.$$('.back').closest('.buttony').removeClass('disabled');
		}
	};

  ResultsSummary.prototype._removeKeyword = function(word){
    var idx = this._keywords.indexOf(word);
    if (idx != -1){
      this._keywords.splice(idx, 1);
      this._redraw();
      this.fire("keywordRemoved", word);
    }
  };

  var DEFAULT_ERROR = "We're Sorry, MediaFinder is experiencing technical difficulties. <br>Please try again later.";
  var TIMEOUT_ERROR = 'Your session has timed out. <br><a href="/?signmeout">Please sign in again</a>';

  var ERROR_MESSAGES = {
    500: DEFAULT_ERROR,
    403: TIMEOUT_ERROR,
    0  : TIMEOUT_ERROR
  };

  return ResultsSummary;
});
