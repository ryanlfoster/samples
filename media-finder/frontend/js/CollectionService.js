define(["pubsub"], function(pubsub){
  function Service(){
	this._rootUrl = "/ws/collections";
	this._userUrl = "/ws/users";
	mixin(pubsub, this);
  }

  Service.prototype.getAll = function(success, error){
	$.get(this._rootUrl)
		.success(success)
		.error(error);
  };

  Service.prototype.get = function(collectionId, success, error){
	var path = this._rootUrl + '/' + collectionId;
	$.get(path)
		.success(success)
		.error(error);
  };

  Service.prototype.create = function(name, success, error){
	$.post(this._rootUrl, {name: name})
		.success(success)
		.error(error);
  };

  Service.prototype.update = function(collectionId, newName, success, error){
	var path = this._rootUrl + '/' + collectionId;
	$.ajax(path, {type: 'PUT', data: {name: newName}})
		.success(success)
		.error(error);
  };

  Service.prototype.destroy = function(collectionId, success, error){
	var path = this._rootUrl + '/' + collectionId;
	$.ajax(path, {type: 'DELETE'})
		.success(success)
		.error(error);
  };

  Service.prototype.getEntries = function(collectionId, success, error){
	var path = this._rootUrl + '/' + collectionId + '/entries';
	$.get(path)
		.success(success)
		.error(error);
  };

  Service.prototype.searchEntries = function(collectionId, term, success, error){
	var path, s, e;
	if(typeof term === 'function'){
		path = this._rootUrl + '/' + collectionId + '/entries/search';
		s = term;
		e = success;
	}else{
		path = this._rootUrl + '/' + collectionId + '/entries/search/' + term;
		s = success;
		e = error;
	}
	$.get(path)
		.success(s)
		.error(e);
  };

  Service.prototype.addEntry = function(collectionId, childId, success, error){
	var path = this._rootUrl + '/' + collectionId + '/entries';
	$.post(path, {childId: childId})
		.success(success)
		.error(error);
  };

  Service.prototype.destroyEntry = function(collectionId, entryId, success, error){
	var path = this._rootUrl + '/' + collectionId + '/entries/' + entryId;
	$.ajax(path, {type: 'DELETE'})
		.success(success)
		.error(error);
  };

  Service.prototype.getUsers = function(collectionId, success, error){
	var path = this._rootUrl + '/' + collectionId + '/users';
	$.get(path)
		.success(success)
		.error(error);
  };

  Service.prototype.addUser = function(collectionId, username, userType, success, error){
	var path = this._rootUrl + '/' + collectionId + '/users';
	$.post(path, {username: username, userType: userType})
		.success(success)
		.error(error);
  };

  Service.prototype.destroyUser = function(collectionId, username, success, error){
	var path = this._rootUrl + '/' + collectionId + '/users/' + username;
	$.ajax(path, {type: 'DELETE'})
		.success(success)
		.error(error);
  };

  Service.prototype.userLookup = function(searchTerm, success, error){
	var path = this._userUrl + '/search/' + searchTerm;
	$.get(path)
		.success(success)
		.error(error);
  };

  return Service;
});
