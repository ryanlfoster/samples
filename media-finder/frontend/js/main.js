require({
  paths:{
    jquery: "lib/jquery",
    handlebars: "lib/handlebars",
    text: "lib/text",
    Backbone: "lib/backbone",
	step: "lib/step"
  }
}, ["tmpl", "jquery", "lib/underscore", "mixin", "lib/date", "lib/jquery.jstree"],
function(tmpl){
  tmpl.rootDir("../tmpl/");
  tmpl.extension(".html");
  require([
    "SearchForm",
    "UrlHashHandler",
    "ResultsSummary",
    "SearchService",
    "IpGroups",
    "Details",
    "FacetOptions",
    "ResultsGrid",
    "popup",
    "omniture",
    "ImageType",
    "ImageOrientation",
    "preferences",
    "SearchInfo",
    "Sorting",
    "Tab",
    "CollectionSummary",
    "Collections",
    "CollectionService",
    "Pending",
    "Help",
    "handlebarsHelpers",
    "polyfills"
  ], function(
    SearchForm,
    UrlHashHandler,
    ResultsSummary,
    SearchService,
    IpGroups,
    Details,
    FacetOptions,
    ResultsGrid,
    popup,
    omniture,
    ImageType,
    ImageOrientation,
    preferences,
    searchInfo,
    Sorting,
    Tab,
    CollectionSummary,
    Collections,
    CollectionService,
    Pending,
    Help
  ){
    var newSearch,
        updateTypeCounts,
        updateOrientationCounts;

    //Set up helper objects
    var url               = new UrlHashHandler();
    var searchService     = new SearchService();
    var collectionService = new CollectionService();

    //Setup UI Components
    var searchForm    = new SearchForm("#searchPoint");
    var pending       = new Pending("#header");
    var help          = new Help("#header");
    var searchSummary = new ResultsSummary("#resultsHeader");

    checkChromeFrame();
    //Search Controls
    var searchTab     = new Tab("#tabs", {name: "Filters", activate: "#controls"});
    var searchResults = new ResultsGrid("#results", searchService);
    var sorting       = new Sorting("#controls");
    var ipGroups      = new IpGroups("#controls");
    var facets        = new FacetOptions("#controls");
    var orientation   = new ImageOrientation("#controls");
    var imageType     = new ImageType("#controls");

    //Collection Controls
    var collectionTab     = new Tab("#tabs", {name: "Collections", style: "collection-icon", activate: "#collections"});
    var collectionSummary = new CollectionSummary("#collectionResults");
    var collectionResults = new ResultsGrid("#collectionResults", collectionService);
    var collections       = new Collections("#collections");

	  searchTab.on('selected', function(){collectionSummary.slideDown()});
	  searchSummary.on('back', function(){collectionSummary.slideDown()});
	  searchSummary.on('forward', function(){collectionSummary.slideDown()});

    omniture.wireUpWithUrl(url);

    url.attachRouteAsClass("body", "route-is-");

    url.on({

      landing: function(params){
        $('#download').hide();
        searchForm.landingPage();
        ipGroups.hide();
        sorting.hide();
        ipGroups.setIpGroups([]);
        orientation.hide();
        orientation.setOrientation([]);
        imageType.hide();
        imageType.setTypes([]);
        searchService.setIpGroups([]);
        searchSummary.setKeywords([]);
        searchService.setKeywords([]);
        facets.hide();
      },

      search: function(params){
        $('#download').hide();
        var query = params.query;
        searchForm.embedded(query);
        searchSummary.loading(query);
        searchResults.kickoffNewSearch();
        facets.hide();
        popup.killCurrent();
		collectionSummary.slideDown();
        if( $('div.tab.selected').length===0 ) { searchTab.selected(); }
      },

      "?query": function(query){
        newSearch = true;
        url.newSearch();
        searchService.setSearchTerm(query);
        url.deleteParam("keywords");
        url.deleteParam("orientation");
        url.deleteParam("imageType");
        this["?keywords"](null);
        this["?orientation"](null);
        this["?imageType"](null);
      },

      "?ipgroups": function(groupList){
        if (groupList){
          var groups = groupList.split(",");
          ipGroups.setIpGroups(groups);
          searchService.setIpGroups(groups);
        }
      },

      "?imageType":function(types){
        updateOrientationCounts = true;
        if(types){
          types = types.split(',');
        }else{
          updateTypeCounts = true;
        }
        imageType.setTypes(types);
        searchService.setTelescopeAssetType(types);
      },

      "?orientation":function(orientations){
        updateTypeCounts = true;
        if(orientations){
          orientations = orientations.split(',');
        }else{
          updateOrientationCounts = true;
        }
        orientation.setOrientation(orientations);
        searchService.setImageOrientation(orientations);
      },

      "?keywords":function(param){
        updateTypeCounts = true;
        updateOrientationCounts = true;
        var keywords = [];
        if (param !== null){
          // parse, but fail silently
          try { keywords = JSON.parse(param); } catch (e) {}
        }
        facets.setCurrentKeywords(keywords);
        searchService.setKeywords(keywords);
        searchSummary.setKeywords(keywords);
      },

      "?sort":function(sort){
        sorting.setSort(sort);
        searchService.setSort(sort);
      },

      "?mode":function(mode){
        mode = mode || 'a';
        $("body").removeClass(function(){
          $(this).attr("class").split(' ').filter(function(c){
            return !!c.match(/^mode/);
          });
        });
        $("body").addClass("mode"+mode.toUpperCase());
      },

      "?dev":function(isDev){
        if (isDev){
          setTimeout(function(){
            $($('.resultImage')[0]).click();
          }, 1500);
        }
      }
    });

    url.startAt("landing");

    searchForm.onSearch(function(term){
      url.set("search", {query: term});
    });

    ipGroups.onSelectGroups(function(groups){
      url.setParam("ipgroups", groups.join(","));
    });

    facets.onKeywordSelected(function(keyword){
        url.addToListParam("keywords", keyword);
    });

    searchSummary.onKeywordRemoved(function(keyword){
      url.removeFromListParam("keywords", keyword);
    });

    searchSummary.onPageChanged(function(page){
      searchResults.loadResultsForPage(page);
    });

    imageType.onTypeSelected(function(types){
      if(types.length === 0){
        url.removeParam('imageType');
      }else{
        url.setParam('imageType', types.join(","));
      }
    });

    orientation.onOrientationSelected(function(orientation){
      if(orientation.length === 0){
        url.removeParam('orientation');
      }else{
        url.setParam('orientation', orientation.join(","));
      }
    });

    sorting.onSort(function(sort){
      url.setParam("sort", sort);
    });

    searchService.onLoad(function(data, criteria){
      if (url.getRoute() === "search"){
        if(newSearch){
          imageType.loadTypes(getFacetType(data, 'telescopeAssetType'));
          orientation.loadOrientation(getFacetType(data, 'imageOrientation'));
          newSearch = false;
          updateTypeCounts = false;
          updateOrientationCounts = false;
        }
        if(updateTypeCounts){
          imageType.loadCounts(getFacetType(data, 'telescopeAssetType'));
          updateTypeCounts = false;
        }
        if(updateOrientationCounts){
          orientation.loadCounts(getFacetType(data, 'imageOrientation'));
          updateOrientationCounts = false;
        }
        ipGroups.show();
        imageType.show();
        orientation.show();
        sorting.show();
        searchSummary.setNumberResults(data.total);
        facets.setKeywords(data);
      }
    });

    searchService.onError(function(error){
      searchSummary.error(error);
      searchResults.errorLoading();
      ipGroups._elem.hide();
      facets.hide();
    });

	  collectionSummary.on('show',function(collEntry){
		  collectionResults.getCollectionEntries(collEntry.id);
	  });

	  collections.on('changecurrent',function(collEntry, show){
		  collectionSummary.setCurrent(collEntry, show);
	  });

    function getFacetType(data, type){
      var facetType = _.where(data.facets, {'facet-type': type});
      if(facetType.length > 0){
        facetType = facetType[0]['facet-value'];
        if(!(facetType instanceof Array))
          facetType = [facetType];
      }else{
        facetType = [];
      }
      return facetType;
    }

    function checkChromeFrame(){
      if ($.browser.msie){
        searchForm.hide();
        require(["ChromeFrameInstaller"], function(Installer){
          Installer.install();
          searchForm.show();
        });
        throw "Not going forward without chromeframe!";
      }
    }
  });
});
