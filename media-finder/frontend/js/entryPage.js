require({
  paths:{
    handlebars: "lib/handlebars",
    text: "lib/text",
    Backbone: "lib/backbone"
  }
}, ["tmpl", "lib/jquery", "lib/underscore", "mixin"],
function(tmpl){
  tmpl.rootDir("../tmpl/");
  tmpl.extension(".html");
  require(["Details", "SearchService", "handlebarsHelpers"],
  function( Details, SearchService){
    var searchService = new SearchService();
    var id = window.location.pathname.split('/').pop();
    searchService.getEntry(id, function(data){
      var details = new Details(data.entries.searchEntry);
      details.show();
    });
  });
});
