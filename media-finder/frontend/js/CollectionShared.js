define(["widget", "pubsub", "CollectionService", "tmpl!CollectionShared", "step"],
function(widget,   pubsub, collectionService, template, step){
	function Shared(data, selector){
		this._template = template;
		this._data = data;
		this._service = new collectionService();

		mixin(pubsub, widget, this).placeInDom(selector);
	}

	Shared.prototype.addUsers = function(data){
		this._data = data;
		this.render(data);
		this.removeLoader();
	};

	Shared.prototype.removeLoader = function(){
		this.$$('.loading').hide();
	};

	Shared.prototype.showLoader = function(){
		this.$$('.loading').show();
	};

	Shared.prototype.refresh = function(){
		var self = this;
		this._service.getUsers(this._data.collectionId, function(data){
			self._data.user = data.user instanceof Array ? data.user : [data.user];
			self.addUsers(self._data);
		}, function(){
			self.removeLoader();
			self.$$('.error').text('There was an error loading the share panel. Please try again later.');
		});
	};

	Shared.prototype._wireUp = function(elem, $$, self){
		$$('.add').on('click', function(){
			var username = $.trim($$('input').val());
			if(username !== ''){
				self._searchForUser(username);
			}
		});

		elem.on('click', '.share', function(e){
			var target = $(e.target);
			var username = target.closest('[data-username]').attr('data-username');
			self._shareWithUser(username);
		});

		$$('.close').on('click', function(){
			self.fire('close');
			elem.remove();
		});

		$$('.delete').on('click', function(){
			self._data.errorMsgs = [];
			self.showLoader();
			var username = $(this).closest('[data-username]').attr('data-username');
			self._service.destroyUser(self._data.collectionId, username, function(){
				self.refresh();
			}, function(){
				self.removeLoader();
				self.$$('.error').text('There was an error removing this user. Please try again later.');
			});
		});

		$$('.options span').on('click', function(e){
			elem.toggleClass('bulk search');
		});

		$$('.bulk .addBulk').on('click', function(e){
			self._data.errorMsgs = [];
			var usernames = $$('.bulk textarea').val().split(',');
			var currentShares = $$('.sharedWith>li');
			self._systematicallyShareWithLots(currentShares, usernames);
			$$('.bulk textarea').val('');
		});

		$$('.shareWith').on('keyup', function(e){
			if(e.keyCode == 13){
				var username = $.trim($$('input').val());
				if(username !== ''){
					self._searchForUser(username);
				}
			}
		});
	};

	Shared.prototype._systematicallyShareWithLots = function(currentShares, usernames){

		//first clean up the array
		// trim spaces off of the usernames
		for(var i in usernames){
			usernames[i] = usernames[i].trim();
		}
		// and filter OUT blank, empty, null or undefined array entries
		usernames = _.without(usernames, "", null, undefined);

		// we had problems sharing with all users all at once. we found that if the collection
		// is already shared with one user then we can share with the rest real fast no prob
		// but we have to get at least 1 successful 1 first

		// no users? bail!!!
		if(usernames.length<1){ return; }

		// if we already have shares then we dont have to worry about async problems
		if(currentShares.length>1) {
			for(var i in usernames ){
				this._shareWithUser(usernames[i]);
			}
			return;
		}

		var allMyFuncs = [];
		// here we build an array of functions we want run one afer the other
		for(var i in usernames){
			var self = this;
			var u = usernames[i];
			(function(u) { // this closure helps with scoping the variable
				var fn = function(isAsync){
					self._shareWithUser(u, this, isAsync)
				};
				allMyFuncs.push(fn);
			})(u);
		}

		allMyFuncs.push(function(){
			//console.log('all done!');
		});

		step.apply(this,allMyFuncs);
	};

	Shared.prototype._searchForUser = function(searchTerm){
		this._data.errorMsgs = [];
		this.showLoader();
		var self = this;
		this._service.userLookup(searchTerm, function(data){
			self.removeLoader();
			self.$$('.error').text('');
			var users = data.user instanceof Array ? data.user : [data.user];
			var subuser = $('<span/>').addClass('subuser');
			var share = $('<span/>').addClass('action share');
			var shareList = $('.searchResults').removeClass('hidden').empty();
			for(var i in users){
				var un = users[i].username;
				$('<li/>')
					.attr('data-username', un)
					.text(users[i].fullName)
					.append(share.clone().attr('title','share with '+un))
					.append(subuser.clone().text(un))
					.appendTo(shareList);
			}
		}, function(data){
			self.removeLoader();
			if(data.status === 404){
				self.$$('.error').text('There were no search results. Please verify spelling.');
			}else{
				self.$$('.error').text('There was an error searching for users. Please try again later.');
			}
		});
	};

	Shared.prototype._shareWithUser = function(username, optCB, optCBasync){
		optCB = optCB || function(){};
		this.showLoader();
		var self = this;
		if(optCBasync){setTimeout(function(){optCB(true)},0);} // truly async
		return this._service.addUser(this._data.collectionId, username, 'CONTRIBUTOR', function(){
			if(!optCBasync){optCB(true);} // allows us to add 1 user, then add all the rest if successful
			self.refresh();
		}, function(){
			if(!optCBasync){optCB(false);} // allows us to add 1 user, then when fail add just the 1 next user
			var msg = 'User: ' + username + ' was not found. Please verify the spelling.';
			self._data.errorMsgs.push(msg);
			self.refresh();
		});
	};

	return Shared;
});
