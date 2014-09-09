define(["widget", "pubsub", "Result"],
function(widget, pubsub, Result){
  var template = function(){ return '<div id="resultsGrid"><span class="intro">Select a collection from the "Collections" tab on the left.</span></div>'; };
  function ResultsGrid(selector, service){
    this._rowsPerWindow = 0;
    this._numPerRow = 0;

    this._service = service;
    this._data = null;
    this._template = template;

    mixin(pubsub, widget, this).placeInDom(selector);

    this._initializeGrid();
  }

  ResultsGrid.prototype.loading = function(){
    this.removeLoadingSpinner();
    if (this._errorLoading){ return; }

    var spinner = $('<div class="loading"></div>');
    this._elem.append(spinner);
  };

  ResultsGrid.prototype.removeLoadingSpinner = function(){
    this.$$(".loading").remove();
  };

  ResultsGrid.prototype._doneLoadingAnimated = function(){
    this.$$(".loading").animate({
      opacity: 0
    });
  };

  ResultsGrid.prototype.errorLoading = function(){
    this._errorLoading = true;
    this.$$(".loading").remove();
    this._elem.fadeOut(500, function(){
    });
  };

  ResultsGrid.prototype.kickoffNewSearch = function(){
    this._getResults(true);
  };

  ResultsGrid.prototype.clearResults = function(){
    this._elem.empty();
  };

  ResultsGrid.prototype._initializeGrid = function(){
    this.render();
  };

	ResultsGrid.prototype.getCollectionEntries = function(collectionId){
		var self = this;
		self._service.searchEntries(collectionId, function(response){
			self.clearResults();
			response = response.entries;

			// make sure results will be an Array
			if(response && response.searchEntry){
				var results = [].concat( response.searchEntry );
				var newResults = results.map(function(data){
					data.collection=true;
					return new Result(self._elem, data);
				});
				self.show();
				self._elem.trigger('scroll').css('padding', '3px 0 15px 0');

			} else { // good response, no results
				$(self._elem).text('This collection has no assets').css('padding', '15px');
			}

		}, function(){
			$(self._elem).text('This collection has no assets').css('padding', '15px');
		});
	};

  ResultsGrid.prototype._wireUp = function(elem, $$, self){
    $(elem).on('scroll', function(e){
      var target = $(e.target),
          top = target.offset().top,
          bottom = top + target.outerHeight();
      $$('.result').each(function(){
        var image = $(this).find('img');
        if(($(this).offset().top - bottom) < bottom && image.attr('data-img')){
          var img = image.attr('data-img');
          image.attr('src', img).removeAttr('data-img');
        }
      });
    });

    $(elem).on('click', '#loadMore', function(e){
      $$('.addMoreResults').text('loading...');
      self._getResults(false);
    });
  };

	ResultsGrid.prototype.loadResultsForPage = function(page){
		var nresults = 100,
			self = this,
			startFrom = (page - 1) * nresults;
		self.clearResults();
		self.loading();
		self._service.search(startFrom, nresults, function(response){
			self.clearResults();
			var results = response || [];
			var newResults = results.map(function(data){
				data.search=true;
				return new Result(self._elem, data);
			});
			self.removeLoadingSpinner();
			self.show();
      self._elem.scrollTop(0);
      self._elem.trigger('scroll');
      setTimeout(function(){
        addPagination(self._elem);
      }, 1000);
		});
	};

	ResultsGrid.prototype._getResults = function(isNew){
		var nresults = 100;
		var self = this;
		self.clearResults();
    self.loading();
		self._service.search(0, nresults, function(response){
			self.clearResults();
			var results = response || [];
			var newResults = results.map(function(data){
				data.search=true;
				return new Result(self._elem, data);
			});
      self.removeLoadingSpinner();
			self.show();
			self._elem.trigger('scroll');
      setTimeout(function(){
        addPagination(self._elem);
      }, 1000);
		});
	};

  function addPagination(elem){
    var pagination = $('#pagination').clone(true);
    $('.currentPage', pagination).remove();
    $('.totalPages', pagination).remove();
    elem.find('#pagination').remove().end().append(pagination);
  }

	return ResultsGrid;
});
