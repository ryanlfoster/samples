define(['https', '../lib/logger', 'xml2js', '../lib/utils', '../lib/helper', 'underscore', 'util'],
	function(https, 		LOG, 	xml2js, 		utils, 			helper,			 _, 	util){
	var config = utils.setupConfig(),
		options = {
			host: config.catalog.host,
			port: config.catalog.port,
			headers:{
				Authorization: config.catalog.auth
			}
		};
	//GET -- /ws/collections
	function getAll(req, res){
		var user = req.header('policy-cn', helper.defaultUser()),
			path = '/entry/collection/usersCollections?user=' + user,
			data = "";

		// copy the OPTIONS obj since this is a shared container, others could change it
		var opt = _.extend({method:'GET',path:path}, options);
		LOG.info(path);
		https.request(opt, function(response){
			var statusCode = response.statusCode.toString();
			if(statusCode[0] !== "2"){//FAILURE
				LOG.info('Getting Collections Failed with status code: ' + statusCode);
				return res.send(response.statusCode);
			}
			response.on('data', function(chunk){
				data += chunk;
			});
			response.on('end', function(){
				var collections = JSON.parse(data);
				var own = false;
				for(var i in collections){
					if(collections[i] === "OWNER")
						own = true;
				}
				if(own)
					return _getCollectionsById(collections, user, res);
				else
					return _createDefault(user, res);
			});
		}).on('error', function(err){
			LOG.info('Getting Collections Failed with error: ' + err);
		}).end();
	}
	//GET -- /ws/collections/:id
	function get(req, res){
		var user = req.header('policy-cn', helper.defaultUser()),
			entryId = req.param('id'),
			path = '/entry/findByEntryId?entryIds=' + entryId + '&user=' + user;

		// copy the OPTIONS obj since this is a shared container, others could change it
		var opt = _.extend({method:'GET',path:path}, options);
		LOG.info(path);
		https.request(opt, function(response){
			var statusCode = response.statusCode.toString();
			if(statusCode[0] !== "2"){//FAILURE
				LOG.info('Getting a Collection Failed with status code: ' + statusCode);
				return res.send(response.statusCode);
			}
			var parser = new xml2js.Parser();
			response.on('data', function(chunk){
				parser.parseString(chunk);
			});

			parser.addListener('end', function(result){
				response.on('end',function(){
					return res.json(result, 200);
				});
			});
		}).on('error', function(err){
			LOG.info('Getting a Collection Failed with error: ' + err);
		}).end();
	}
	//POST -- /ws/collections
	//data: name
	function create(req, res){
		var user = req.header('policy-cn', helper.defaultUser()),
			name = req.body.name,
			path = "/entry/collection/create?name=" + encodeURI(name) + "&owner=" + user + "&user=" + user;

		// copy the OPTIONS obj since this is a shared container, others could change it
		var opt = _.extend({method:'POST',path:path}, options);
		LOG.info(path);
		https.request(opt, function(response){
			var statusCode = response.statusCode.toString();
			if(statusCode[0] !== "2"){//FAILURE
				LOG.info('Creating a Collection Failed with status code: ' + statusCode);
			}
			var parser = new xml2js.Parser();
			response.on('data', function(chunk){
				parser.parseString(chunk);
			});

			parser.addListener('end', function(result){
				response.on('end',function(){
					return res.json(result, response.statusCode);
				});
			});
		}).on('error', function(err){
			LOG.info('Creating a Collection Failed with error: ' + err);
		}).end();
	}
	//PUT -- /ws/collections/:id
	function update(req, res){
		var user = req.header('policy-cn', helper.defaultUser()),
			entryId = req.param('id'),
			name = req.body.name,
			path = "/entry/collection/update/" + entryId + "?user=" + user + "&name=" + encodeURI(name);

		// copy the OPTIONS obj since this is a shared container, others could change it
		var opt = _.extend({method:'POST',path:path}, options);
		LOG.info(path);
		https.request(opt, function(response){
			var statusCode = response.statusCode.toString();
			if(statusCode[0] !== "2"){//FAILURE
				LOG.info('Updating a Collection Failed with status code: ' + statusCode);
				return res.send(response.statusCode);
			} else if (statusCode === '204') { // NO CONTENT means NO DATA
				return res.send(response.statusCode);
			}
			var parser = new xml2js.Parser();
			response.on('data', function(chunk){
				parser.parseString(chunk);
			});

			parser.addListener('end', function(result){
				response.on('end',function(){
					return res.json(result, 200);
				});
			});
		}).on('error', function(err){
			LOG.info('Updating a Collection Failed with error: ' + err);
		}).end();
	}
	//DELETE -- /ws/collections/:id
	function destroy(req, res){
		var user = req.header('policy-cn', helper.defaultUser()),
			entryId = req.param('id'),
			path = "/entry/collection/delete/" + entryId + "?user=" + user;

		// copy the OPTIONS obj since this is a shared container, others could change it
		var opt = _.extend({method:'DELETE',path:path}, options);
		https.request(opt, function(response){
			var statusCode = response.statusCode.toString();
			if(statusCode[0] !== "2"){//FAILURE
				LOG.info('Deleting a Collection Failed with status code: ' + statusCode);
				return res.send(response.statusCode);
			}
			return res.send(response.statusCode);
		}).on('error', function(err){
			LOG.info('Deleting a Collection Failed with error: ' + err);
		}).end();
	}
	//GET -- /ws/collections/:id/entries
	function getEntries(req, res){
		var user = req.header('policy-cn', helper.defaultUser()),
			entryId = req.param('id'),
			path = "/entry/collection/getAll/" + entryId + "?user=" + user;

		// copy the OPTIONS obj since this is a shared container, others could change it
		var opt = _.extend({method:'GET',path:path}, options);
		LOG.info(path);
		https.request(opt, function(response){
			var statusCode = response.statusCode.toString();
			if(statusCode[0] !== "2"){//FAILURE
				LOG.info('Getting entries for a Collection Failed with status code: ' + statusCode);
				return res.send(response.statusCode);
			}
			var parser = new xml2js.Parser();
			response.on('data', function(chunk){
				parser.parseString(chunk);
			});

			parser.addListener('end', function(result){
				response.on('end',function(){
					return res.json(result, 200);
				});
			});
		}).on('error', function(err){
			LOG.info('Getting entries for a Collection Failed with error: ' + err);
		}).end();
	}
	//GET -- /ws/collections/:id/entries/search(/:term)
	function searchEntries(req, res){
		var user = req.header('policy-cn', helper.defaultUser()),
			id = req.param('id'),
			term = req.param('term') || " ",
			path = "/entry/search?user=" + user + "&q=" + encodeURI(term) + "&rootEntryId=" + id + "&facets=assetType:IMAGE";

		// copy the OPTIONS obj since this is a shared container, others could change it
		var opt = _.extend({method:'GET',path:path}, options);
		LOG.info(path);
		https.request(opt, function(response){
			var statusCode = response.statusCode.toString();
			if(statusCode[0] !== "2"){//FAILURE
				LOG.info('Searching entries for a Collection Failed with status code: ' + statusCode);
				return res.send(response.statusCode);
			}
			var parser = new xml2js.Parser();
			response.on('data', function(chunk){
				parser.parseString(chunk);
			});

			parser.addListener('end', function(result){
				response.on('end',function(){
					return res.json(result, 200);
				});
			});
		}).on('error', function(err){
			LOG.info('Searching entries for a Collection Failed with error: ' + err);
		}).end();
	}
	//POST -- /ws/collections/:id/entries
	//data: childId
	function addEntry(req, res){
		var user = req.header('policy-cn', helper.defaultUser()),
			entryId = req.param('id'),
			childId = req.body.childId,
			path = "/entry/collection/addItems/" + entryId + "?childEntryIds=" + childId + "&user=" + user;

		// copy the OPTIONS obj since this is a shared container, others could change it
		var opt = _.extend({method:'POST',path:path}, options);
		LOG.info(path);
		https.request(opt, function(response){
			var statusCode = response.statusCode.toString();
			if(statusCode[0] !== "2"){//FAILURE
				LOG.info('Adding an Item to a Collection Failed with status code: ' + statusCode);
				return res.send(response.statusCode);
			}
			var parser = new xml2js.Parser();
			response.on('data', function(chunk){
				parser.parseString(chunk);
			});

			parser.addListener('end', function(result){
				response.on('end',function(){
					return res.json(result, 200);
				});
			});
		}).on('error', function(err){
			LOG.info('Adding an Item to a Collection Failed with error: ' + err);
		}).end();
	}
	//DELETE -- /ws/collections/:id/entries/:entryId
	function destroyEntry(req, res){
		var user = req.header('policy-cn', helper.defaultUser()),
			id = req.param('id'),
			entryId = req.param('entryId'),
			path = "/entry/collection/removeItems/" + id + "?childEntryIds=" + entryId + "&user=" + user;

		// copy the OPTIONS obj since this is a shared container, others could change it
		var opt = _.extend({method:'POST',path:path}, options);
		LOG.info(path);
		https.request(opt, function(response){
			var statusCode = response.statusCode.toString();
			if(statusCode[0] !== "2"){//FAILURE
				LOG.info('Deleting an entry from a Collection Failed with status code: ' + statusCode);
				return res.send(response.statusCode);
			}
			return res.send(response.statusCode);
		}).on('error', function(err){
			LOG.info('Deleting an entry from a Collection Failed with error: ' + err);
		}).end();
	}
	//GET -- /ws/collections/:id/users
	function getUsers(req, res){
		var user = req.header('policy-cn', helper.defaultUser()),
			entryId = req.param('id'),
			path = "/entry/collection/getUsers/" + entryId + "?user=" + user;

		// copy the OPTIONS obj since this is a shared container, others could change it
		var opt = _.extend({method:'GET',path:path}, options);
		LOG.info(path);
		https.request(opt, function(response){
			var statusCode = response.statusCode.toString();
			if(statusCode[0] !== "2"){//FAILURE
				LOG.info('Getting a Collection\'s users Failed with status code: ' + statusCode);
				return res.send(response.statusCode);
			}
			var parser = new xml2js.Parser();
			response.on('data', function(chunk){
				parser.parseString(chunk);
			});

			parser.addListener('end', function(result){
				response.on('end',function(){
					return res.json(result, 200);
				});
			});
		}).on('error', function(err){
			LOG.info('Getting a Collection\'s users Failed with error: ' + err);
		}).end();
	}
	//POST -- /ws/collections/:id/users
	//data: username
	//data: userType (OWNER, CONTRIBUTOR, or CONSUMER)
	function addUser(req, res){
		var user = req.header('policy-cn', helper.defaultUser()),
			entryId = req.param('id'),
			username = req.body.username,
			userType = req.body.userType,
			path = "/entry/collection/addUser/" + entryId + "?userAdd=" + username + "&userType=" + userType + "&user=" + user;

		// copy the OPTIONS obj since this is a shared container, others could change it
		var opt = _.extend({method:'POST',path:path}, options);
		LOG.info(opt.host+path);
		https.request(opt, function(response){
			var statusCode = response.statusCode.toString();
			if(statusCode[0] !== "2"){//FAILURE
				LOG.info('Adding a User to a Collection Failed with status code: ' + statusCode);
			}
			response.on('end',function(){
				return res.send(response.statusCode);
			});
		}).on('error', function(err){
			LOG.info('Adding a User to a Collection Failed with error: ' + err);
		}).end();
	}
	//DELETE -- /ws/collections/:id/users/:username
	function destroyUser(req, res){
		var user = req.header('policy-cn', helper.defaultUser()),
			entryId = req.param('id'),
			username= req.param('username'),
			path = "/entry/collection/removeUser/" + entryId + "?userRemove=" + username + "&user=" + user;

		// copy the OPTIONS obj since this is a shared container, others could change it
		var opt = _.extend({method:'POST',path:path}, options);
		LOG.info(opt.host+path);
		https.request(opt, function(response){
			var statusCode = response.statusCode.toString();
			if(statusCode[0] !== "2"){//FAILURE
				LOG.info('Removing a User to a Collection Failed with status code: ' + statusCode);
				return res.send(response.statusCode);
			}
			return res.send(response.statusCode);
		}).on('error', function(err){
			LOG.info('Removing a User to a Collection Failed with error: ' + err);
		}).end();
	}

	//GET -- /ws/users/search/:term
	function userSearch(req, res){
		var user = req.header('policy-cn', helper.defaultUser()),
			term = req.param('term'),
			path = "/user/lookup?q=" + encodeURI(term) + "&user=" + user;

		// copy the OPTIONS obj since this is a shared container, others could change it
		var opt = _.extend({method:'GET',path:path}, options);
		LOG.info(path);
		https.request(opt, function(response){
			var statusCode = response.statusCode.toString();
			if(statusCode[0] !== "2"){//FAILURE
				LOG.info('Searching for users Failed with status code: ' + statusCode);
				return res.send(response.statusCode);
			}
			var parser = new xml2js.Parser();
			response.on('data', function(chunk){
				parser.parseString(chunk);
			});

			parser.addListener('end', function(result){
				response.on('end',function(){
					return res.json(result, 200);
				});
			});
		}).on('error', function(err){
			LOG.info('Searching for users Failed with error: ' + err);
		}).end();
	}

	/***********
	*PRIVATE METHODS
	************/
	function _getCollectionsById(data, user, res){
		var entryIds = [];
		for(var i in data){
			entryIds.push(i);
		}
		var path = '/entry/findByEntryId?entryIds=' + entryIds.join(',') + '&user=' + user;

		// copy the OPTIONS obj since this is a shared container, others could change it
		var opt = _.extend({method:'GET',path:path}, options);
		LOG.info(path);
		https.request(opt, function(response){
			var statusCode = response.statusCode.toString();
			if(statusCode[0] !== "2"){//FAILURE
				LOG.info('Getting All Collection data Failed with status code: ' + statusCode);
				return res.send(response.statusCode);
			}
			var parser = new xml2js.Parser();
			response.on('data', function(chunk){
				parser.parseString(chunk);
			});

			parser.addListener('end', function(result){
				response.on('end',function(){
					var collectionTrees = [];
					var entries = result.entry instanceof Array ? result.entry : [result.entry];
					var myCollections = [];
					var sharedCollections = [];

					for(var i in entries){
						var e = entries[i];
						e.metadata.ownership = data[e.id];
						if(data[e.id] === "OWNER"){
							myCollections.push(e);
						}else{
							sharedCollections.push(e);
						}
					}

					var myKids = _setUpDefaults(myCollections, 'ownprivate'), // the client side will call back later to change some of these to 'ownshared'
						myState = myKids.length>0?'open':undefined,
						sharedKids = _setUpDefaults(sharedCollections, 'contribshared'),
						sharedState = sharedKids.length>0?'open':undefined;

					collectionTrees.push({data:"My Collections",children:myKids, state:myState, attr:{rel:'ownroot'}});
					collectionTrees.push({data:"Shared Collections",children:sharedKids, state:sharedState, attr:{rel:'contribroot'}});

					return res.json(collectionTrees, 200);
				});
			});
		}).on('error', function(err){
			LOG.info('Getting All Collection data Failed with error: ' + err);
		}).end();
	}
    // set up default state of nodes shown in jsTree
	function _setUpDefaults(data, type){


		var map = {},
			root = [];

		// first build a map using the id as the KEY and the object itself as the VALUE
		for (var i in data){
			var d = data[i];
			//Creating an Object that JSTree will understand
			var entry = {
				data: d.metadata.name,
				attr: { id: d.id, "data-name": d.metadata.name, "data-ownership": d.metadata.ownership, rel:type },
				parent: d.parentCollection, //This is used only to map an object to its parent
				children: []
			};
			var desc = d.metadata.description ? JSON.parse(d.metadata.description) : undefined;
			if(desc && desc.favorite === true){
				entry.attr['data-favorite'] = true;
			}
			map[d.id] = _.defaults(d, entry);
		}

		// second we build hierarchy using quick lookup from map
		// important to know that we are using the REFERENCES of the objects
		for (var i in map){
			var m = map[i];
			if(helper.is_empty(m.parent)){
				// no parent? must be a root level collection
				root.push(m);
			}else if (m.parent.item['@']){
				// we DO have a parents listed
				if(map[m.parent.item['@'].id]){
					// AND the parent is in the map! NEST IT NEST IT
					map[m.parent.item['@'].id]['children'].push(m);
				} else {
					// the parents isn't in the map :-(
					// i.e. a child collection was shared with us, we cant see parent
					// put it on the root level
					root.push(m);
				}
			}
			delete m.parent; //This variable is then deleted because JSTree does not need it
		}

        return root;
	}

	//Create default collection if there are none
	function _createDefault(user, res){
		var name = "Favorites",
			description = { favorite: true};
			path = "/entry/collection/create?name=" + encodeURI(name) + "&owner=" + user + "&user=" + user + "&description=" + JSON.stringify(description);

		// copy the OPTIONS obj since this is a shared container, others could change it
		var opt = _.extend({method:'POST',path:path}, options);
		LOG.info(path);
		https.request(opt, function(response){
			var statusCode = response.statusCode.toString();
			if(statusCode[0] !== "2"){//FAILURE
				LOG.info('Creating a Collection Failed with status code: ' + statusCode);
			}

			response.on('end',function(){
				return res.redirect('/ws/collections');
			});

		}).on('error', function(err){
			LOG.info('Creating a Collection Failed with error: ' + err);
		}).end();
	}

	return {
		getAll: getAll,
		get: get,
		create: create,
		update: update,
		destroy: destroy,
		getEntries: getEntries,
		searchEntries: searchEntries,
		addEntry: addEntry,
		destroyEntry: destroyEntry,
		getUsers: getUsers,
		addUser: addUser,
		destroyUser: destroyUser,
		userSearch: userSearch
	};
});