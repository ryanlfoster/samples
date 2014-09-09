define(["pubsub", "preferences"], function(pubsub, preferences){
  function UrlHashHandler(){
    this._hash = {href: "", params: {}};

    mixin(pubsub, this);
    this._changeClass = function(){};
    this._watchHash();
  }

  UrlHashHandler.prototype.set = function(hashUrl, params){
    this._hash.href = hashUrl;
    for (var key in params) {
      if (params[key] === null){
        delete this._hash.params[key];
      } else {
        this._hash.params[key] = params[key];
      }
    }
    this._setHash();
  };

  UrlHashHandler.prototype.setParam = function(p, v){
    this._hash.params[p] = v;
    this._setHash();
  };

  UrlHashHandler.prototype.deleteParam = function(p){
    delete this._hash.params[p];
  };

  UrlHashHandler.prototype.removeParam = function(p){
    delete this._hash.params[p];
    this._setHash();
  };

  UrlHashHandler.prototype.addToListParam = function(param, newItem){
    var items = [];
    try {
      items = JSON.parse(this._hash.params[param]);
    } catch (e) {
      //the param was undefined or not valid JSON
      items = [];
    }
    items.push(newItem);
    this._hash.params[param] = JSON.stringify(items);
    this._setHash();
  };

  UrlHashHandler.prototype.removeFromListParam = function(param, killItem){
    if (this._hash.params[param] === undefined){ return; }
    var items = [];
    try {
      items = JSON.parse(this._hash.params[param]);
    } catch (e) {} //items undefined, or not JSON
    var idx = items.indexOf(killItem);
    if (idx !== -1){
      items.splice(idx, 1);
      if (items.length === 0){
        delete this._hash.params[param];
      } else {
        this._hash.params[param] = JSON.stringify(items);
      }
      this._setHash();
    }
  };

  UrlHashHandler.prototype.getRoute = function(){
    return this._hash.href;
  };

  UrlHashHandler.prototype.startAt = function(url){
    var lastLocation;
    this._startingPoint = url;
    if (window.location.hash === ""){
       lastLocation = preferences.get("lastLocation");
       if (lastLocation !== undefined){
        window.location.hash = lastLocation;
        this._getHash();
      } else {
        this.set(url);
      }
    } else {
      this._getHash();
    }
  };

  UrlHashHandler.prototype.attachRouteAsClass = function(selector, prefix){
    selector = selector || "body";
    prefix = prefix || "";
    this._changeClass = function(oldRoute, newRoute){
      $(selector).removeClass(prefix+oldRoute).addClass(prefix+newRoute);
    };
  };
  UrlHashHandler.prototype.newSearch = function(){
    var hash = cleanHash(window.location.hash);
    window.location.hash = hash;
  };

  UrlHashHandler.prototype._watchHash = function(){
    var self = this;
    window.onhashchange = function(){
      self._getHash();
    };
  };

  //todo: rename _getHash and _setHash to reflect that they're syncing up with
  //      the hash, not getting it
  UrlHashHandler.prototype._setHash = function(){
    var oldHash = parseHash(window.location.hash);
    var newHash = this._hash;
    fireChangeEvents(oldHash, newHash, this);
    window.location.hash = hashToString(this._hash);
    preferences.set("lastLocation", window.location.hash);
  };

  UrlHashHandler.prototype._getHash = function(){
    var oldHash = this._hash;
    var newHash = parseHash(window.location.hash);
    fireChangeEvents(oldHash, newHash, this);
    this._hash.href = newHash.href;
    this._hash.params = newHash.params;
    preferences.set("lastLocation", cleanHash(window.location.hash));
  };

  function cleanHash(hash){
    hash = parseHash(hash);
    var newHash = '#' + hash.href + '?';
    var params = hash.params;
    for(var i in params){
      if(i === 'query' || i === 'ipgroups' || i === 'keywords'){
        newHash += i + '=' + params[i] + '&';
      }
    }
    return newHash.slice(0,-1);
  }

  function parseHash(url){
    var params, hashParts, paramParts;
    var urlParts = url.split('#');

    if (urlParts.length < 2){return {href:"", params:{}};}

    //rejoin on hash in case there was one in a param
    var hash = urlParts.slice(1).join('#');

    hashParts = hash.split("?");

    if (hashParts.length >= 2){
      hash = hashParts[0];
      //rejoin on ? in case there was one in the parameters
      params = hashParts.slice(1).join('?');
    }

    var result = {
      href: hash,
      params: {}
    };

    if (params !== undefined){
      params.split("&").forEach(function(p){
        var paramParts = p.split("=");
        var key = paramParts[0];
        var val = paramParts.slice(1).join("=");
        result.params[key] = val;
      });
    }
    return result;
  }

  function hashToString(hash){
    var key,
        pairs = [],
        queryString = "";

    for (key in hash.params) {
      pairs.push(key + "=" + hash.params[key]);
    }

    if (pairs.length > 0){
      queryString = "?" + pairs.join("&");
    }

    return hash.href + queryString;
  }

  function fireChangeEvents(oldHash, newHash, events){
    var changed = false;

    //find the keys that have changed and report them
    for (var key in newHash.params) {
      if (!(key in oldHash.params) ||
           newHash.params[key] !== oldHash.params[key]){
        events.fire('?' + key, newHash.params[key], newHash.params);
        changed = true;
      }
    }

    //find the keys that have been deleted and report them
    for (key in oldHash.params) {
      if (!(key in newHash.params)){
        events.fire('?' + key, null, newHash.params);
        changed = true;
      }
    }

    changed = changed || oldHash.href != newHash.href;

    if (changed){
      events.fire(newHash.href, newHash.params);
      events._changeClass(oldHash.href, newHash.href);
    }
  }

  return UrlHashHandler;
});
