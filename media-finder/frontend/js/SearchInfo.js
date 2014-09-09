define(function(){
  var KEY_STRING = "searchInfo";
  var localStorage = window.localStorage;
  var searchInfo = null;

  try {
    searchInfo = JSON.parse(localStorage.getItem(KEY_STRING));
  } catch (e) {}

  if (searchInfo === null){
    searchInfo = {};
  }

  return {
    get:function(key, def){
      var pref = searchInfo[key];
      if (pref === undefined){
        if (def !== undefined){
          searchInfo[key] = def;
          this.savesearchInfo();
          return def;
        }
      } else {
        return pref;
      }
    },
    set:function(pref, val){
      searchInfo[pref] = val;
      localStorage.setItem(KEY_STRING, JSON.stringify(searchInfo));
    }
  };
});
