define(function(){
  var KEY_STRING = "userPreferences";
  var localStorage = window.localStorage;
  var preferences = null;

  try {
    preferences = JSON.parse(localStorage.getItem(KEY_STRING));
  } catch (e) {}

  if (preferences === null){
    preferences = {};
  }

  return {
    get:function(key, def){
      var pref = preferences[key];
      if (pref === undefined){
        if (def !== undefined){
          preferences[key] = def;
          this.savePreferences();
          return def;
        }
      } else {
        return pref;
      }
    },
    set:function(pref, val){
      preferences[pref] = val;
      localStorage.setItem(KEY_STRING, JSON.stringify(preferences));
    }
  };
});
