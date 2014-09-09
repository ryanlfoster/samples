define(["pubsub"], function(pubsub){
  var CACHE_SIZE = 50;

  function Ajax(){
    //setting up defaults
    this._criteria = {
      sort: "descending"
    };
    this._newQuery = true;
    mixin(pubsub, this);
    this._previous = 0;
    //Never cache more than three pages worth of results
    this._cache = {};
  }

  Ajax.prototype.getRequestStatus = function(callback){
    var self = this;
    $.getJSON('ws/requests').success(callback).error(function(err, textStatus, errorThrown){
        self.fire("error", err.status);
    });
  };

  Ajax.prototype.askForPermission = function(externalId, source, callback){
    var self = this;
    $.get('/ws/ask/permission', {externalId: externalId, source: source})
        .success(function(data){
          callback(true);
        })
        .error(function(err, textStatus, errorThrown){
          if(err.status === 403){
            callback(false);
          }else{
            self.fire("error", err.responseText);
          }
        });
  };

  Ajax.prototype.requestPermission = function(externalId, source, reason, callback){
    var self = this;
    $.ajax('/ws/request/permission',{
      data:{externalId: externalId, source: source, reason: reason},
      success:function(data){
        callback(data);
      },
      error:function(err, textStatus, errorThrown){
        if($(err.responseText).text()){
          self.fire("error", $(err.responseText).text());
        }else{
          self.fire("error", err.responseText);
        }
      }
    });
  };

  Ajax.prototype.search = function(start, numToLoad, callback){
    var self = this,
        previous = self._previous || 0,
        forward = previous + numToLoad,
        backward = previous - numToLoad;
    if(this._newQuery){
      previous = 0;
      this._cache = {};
    }
    if(start == forward){ //We are moving forward
      if(self._cache[start]){
        callback(self._cache[start]);
        self._searchAjax(start + numToLoad, numToLoad, function(results){
          self._cache[start + numToLoad] = results;
          delete self._cache[start - (numToLoad * 2)];
        });
      }else{
        self._searchAjax(start, numToLoad, function(results){
          callback(results);
          self._cache[start] = results;
          self._searchAjax(start + numToLoad, numToLoad, function(results){
            self._cache[start + numToLoad] = results;
            delete self._cache[start - (numToLoad * 2)];
          });
        });
      }
    }else if(start == backward){ //We are moving backwards
      if(self._cache[start]){
        callback(self._cache[start]);
        self._searchAjax(start - numToLoad, numToLoad, function(results){
          self._cache[start - numToLoad] = results;
          delete self._cache[start + (numToLoad * 2)];
        });
      }else{
        self._searchAjax(start, numToLoad, function(results){
          callback(results);
          self._cache[start] = results;
          self._searchAjax(start - numToLoad, numToLoad, function(results){
            self._cache[start - numToLoad] = results;
            delete self._cache[start + (numToLoad * 2)];
          });
        });
      }
    }else{ //A new search or entered number
      self._cache = {};
      self._searchAjax(start, numToLoad, function(results){
        callback(results);
        self._cache[start] = results;
        self._searchAjax(start + numToLoad, numToLoad, function(results){
          self._cache[start + numToLoad] = results;
        });
      });
    }
    self._previous = start;
  };

  Ajax.prototype.getEntry = function(id, callback){
    $.ajax('/ws/entry/'+id,{
      dataType: "json",
      success: callback,
      error:function(err, textStatus, errorThrown){
        self.fire("error", err.status);
      }
    });
  };

  Ajax.prototype.setSearchTerm = function(term){
    this._criteria.query = term;
    this._newQuery = true;
  };

  Ajax.prototype.setKeywords = function(keywords){
    this._criteria.keywords = keywords;
    this._newQuery = true;
  };

  Ajax.prototype.setAssetType = function(type){
    if (type === null || type === "all"){
      delete this._criteria.types;
    } else {
      this._criteria.types = type;
    }
    this._newQuery = true;
  };

  Ajax.prototype.setIpGroups = function(groups){
    if (groups === null || groups.length === 0){
      delete this._criteria.ipgroups;
    } else {
      this._criteria.ipgroups = groups.join(',');
    }
    this._newQuery = true;
  };

  Ajax.prototype.setTelescopeAssetType = function(types){
    if (types === null || types.length === 0){
      delete this._criteria.teletypes;
    } else {
      this._criteria.teletypes = types.join(',');
    }
    this._newQuery = true;
  };

  Ajax.prototype.setImageOrientation = function(orientation){
    if (orientation === null || orientation.length === 0){
      delete this._criteria.orientation;
    } else {
      this._criteria.orientation = orientation.join(',');
    }
    this._newQuery = true;
  };

  Ajax.prototype.setSort = function(sort){
    if (sort === null){
      sort = "ascending";
    }
    this._criteria.sort = sort;

    this._newQuery = true;
  };

  Ajax.prototype.onLoad = function(callback){
    this.on("load", callback);
  };

  Ajax.prototype.onError = function(callback){
    this.on("error", callback);
  };

  Ajax.prototype._searchAjax = function(start, numToLoad, callback){
    var self = this;
    this._criteria.start = start;
    this._criteria.nResults = numToLoad;
    var num  = numToLoad;
    $.ajax('ws/search',{
      dataType:"json",
      data:this._criteria,
      success:function(data){
        if (self._newQuery){
          self.fire("load", data, self._criteria);
          self._newQuery = false;
        }
        callback(data.results);
      },
      error:function(err, textStatus, errorThrown){
        self.fire("error", err.status);
      }
    });
  };

  return Ajax;
});
