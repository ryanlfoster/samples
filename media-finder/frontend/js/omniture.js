define(["https://ldscdn.org/30/scripts/s_code_ldsall.js "], function(){
  //todo: figure out why there's a race condition for
  //      loading omniture
  //todo: get the names of important contextData variables to colin

  if (typeof s === "undefined"){
    console.log("Omniture code failed to load properly... continuing without it.");
    function noop(){}
    return {
      wireUpWithUrl:noop,
      reportDownload: noop,
      reportDetailOpened: noop
    };
  }

  //omniture stuff
  try {
    var s_code=s.t();if(s_code)document.write(s_code)
  } catch(e){/*fail silently*/}

  var routes = {
    search:function(params){
      /* Give each page an identifying name and channel on the next lines. */
      s.pageName="MediaFinder:Search"
      s.channel="MediaFinder:Search"
      s.events = "event36";
      s.eVar36 = s.prop36 = params.query;
      try {
        s.t();
      } catch(e){/*fail silently*/}
    },

    "?keywords":function(param){
      var keywords = [];
      if (param !== null){
        // parse, but fail silently
        try { keywords = JSON.parse(param); } catch (e) {}
      }
      s.contextData.SelectedKeywordFacets = keywords.join(' AND ');
    },

    "?ipgroups": function(groupList){
      if (groupList){
        var groups = groupList.split(",");
        s.contextData.SelectedIPGroups = groups.join(' AND ');
      }
    }
  };

  return {
    wireUpWithUrl: function wireUp(url){
      url.on(routes);
    },
    reportDownload: function(id){
      try {
        s.tl(true, 'd', 'download of asset: '+id);
      } catch(e){/*fail silently*/}
    },
    reportDetailOpened: function(id){
      s.contextData.DetailsOpenedForAssetId = id;
      try {
        s.t();
      } catch(e){/*fail silently*/}
      s.contextData.DetailsOpenedForAssetId = "";
    }
  }
});
