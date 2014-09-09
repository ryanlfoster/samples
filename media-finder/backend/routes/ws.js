define(['https', '../lib/utils', '../lib/logger', '../lib/helper', 'xml2js', 'util'],
	function(https, 	utils, 				LOG, 		helper, 	xml2js,   util){
	var config = utils.setupConfig();
	function search(req, res){

		var query		= escape(req.param('query', "")),
			keywords	= req.param('keywords', []),
			types		= req.param('types', ""),
			teleTypes	= req.param('teletypes', ""),
			orientation	= req.param('orientation', ""),
			ipGroups	= req.param('ipgroups', "open").split(","),
			start		= req.param('start', "0"),
			sort		= req.param('sort', ""),
			nResults	= parseInt(req.param('nResults', "10"), 10),
			user		= req.header('policy-cn', helper.defaultUser()),
			params		= {user:user, q:query, start:start, resultsPerPage: nResults},
			url			= config.catalog.searchUrl,
			facets		= [],
			path, ipCodes;
		
		//start with the keyword facets
		if (keywords.length){
			facets = keywords.map(function(word){
				return "keyword:" + escape("\"" + word + "\"");
			});
		}

		//build the type facet (there's only one)
		if (types !== ""){
			facets.push("assetType:"+types.toUpperCase());
		}

		//build the telescope asset types
		if (orientation !== ""){
			orientation = orientation.split(',');
			facets.push('(imageOrientation:' + orientation.join('+OR+imageOrientation:') + ')');
		}

		//build the telescope asset types
		if (teleTypes !== ""){
			teleTypes = teleTypes.split(',');
			facets.push(encodeURI('(telescopeAssetType:"' + teleTypes.join('"+OR+telescopeAssetType:"') + '")'));
		}

		//build the sortDirection
		if(sort !== ""){
			params.sortDirection = sort;
			params.sortField = "externalId"; //currently the only field that catalog supports for sorting
		}

		//build the ip facets
		ipCodes = [];
		ipGroups.forEach(function(group){
			ipCodes = ipCodes.concat(utils.IP_CODE_GROUPS[group]);
		});
		facets.push("(ipCode:"+ipCodes.join('+OR+ipCode:')+")");

		params.facets = facets.join('+AND+');

		path = url + utils.urlParams(params);

		LOG.info(path);

		var options = {
			host: config.catalog.host,
			path: path,
			port: config.catalog.port,
			headers:{
				Authorization: config.catalog.auth
			}
		};
		utils.delegate(req, res, options, _translate);
	}

	function _translate(xml){
		//safely traverse the path, ensure it's an array, then map over it
		var results = (xml.entries || {}).searchEntry || (xml.entries || {}).entry || [];
		results = results instanceof Array ? results : [results];
		
		return {
			results: results,
			facets : ((xml.facets || {}).facet || []).filter(function(facet){
				return facet['facet-value'] !== undefined;
			}),
			total: (((xml.entries || {})["@"] || {}).total) || 0,
			xml:xml
		};
	}

	function entry(req, res){
		var id   = req.param("id"),
		user = req.header('policy-cn', helper.defaultUser());

		//todo: this is a duplicate of above! refactor out
		if (user === ""){
			if (config.bypassWam === true){
				user = "catalog";
			} else {
				res.send(403);
				return;
			}
		}
		utils.delegate(req, res, {
			host: config.catalog.host,
			path: config.catalog.searchUrl+'?user='+user+"&q="+id,
			port: config.catalog.port,
			headers:{
				Authorization: config.catalog.auth
			}
		});
	}

	function downloadHandler(req, res){
		var vId  = req.param("vId"),
			rend = req.param("rendering"),
			user = req.header('policy-cn', helper.defaultUser());

		if (rend === undefined){
			downloadFailed(vId, user);
			return;
		}

		if (user === ""){
			res.send(403);
			return;
		}

		var path = "/package/original/sync/" + vId + "?rendering=" + rend + "&user=" + user;
		LOG.info(config.catalog.host + path);
		var options = {
			host: config.catalog.host,
			path: path,
			port: config.catalog.port,
			headers:{
				Authorization: config.catalog.auth
			}
		};

		var dlReq = https.request(options, function(dlRes){
			if (dlRes.statusCode != "200"){
				downloadFailed(vId, user);
				LOG.info(dlRes.statusCode);
				return;
			}

			//These headers totally break IE8 so we check them and discard them
			var ie8Headers = ['vary', 'cache-control', 'pragma'];
			console.log(dlRes.headers);
			for (var header in dlRes.headers){
				if(ie8Headers.indexOf(header.toLowerCase()) === -1){
					res.header(header, dlRes.headers[header]);
				}
			}

			dlRes.on('data', function(chunk){
				res.write(chunk);
			});

			dlRes.on('end', function(chunk){
				res.end();
			});
		});

		dlReq.on('error', function(err){
			LOG.info("DL ERROR");
			downloadFailed(vId, user);
			return;
		});

		dlReq.end();

		function downloadFailed(assetId, user){
			var errorMsg = "The resource you requested cannot be downloaded at this time.";
			var html = "<html><head><script> window.opener.displayErrorMsg('%s', '%s', '%s'); window.close(); </script></head></html>";
			res.send(util.format(html, assetId, user, errorMsg), 200);
		}
	}

	function downloadRequests(req, res){
		var source = req.param('source'),
			externalId = req.param('externalId'),
			user = req.header('policy-cn', helper.defaultUser()),
			path = "/package/original/getRequests?username=" + user + "&user=" + user + "&startDate=",
			daysAgo = new Date();
		daysAgo.setDate(daysAgo.getDate() - 30);
		var day = helper.addZero(daysAgo.getDate()),
			month = helper.addZero(daysAgo.getMonth()),
			year = daysAgo.getFullYear();
		path += day + '-' + month + '-' + year;
		LOG.info(path);
		var options = {
			host: config.catalog.host,
			path: path,
			port: config.catalog.port,
			headers:{
				Authorization: config.catalog.auth
			}
		};
		utils.delegate(req, res, options, null);
	}

	function requestDownload(req, res){
		var source = req.param('source'),
			externalId = req.param('externalId'),
			user = req.header('policy-cn', helper.defaultUser()),
			path = "/package/original/authorize/" + source + "/" + externalId + "?user=" + user,
			options = {
				host: config.catalog.host,
				path: path,
				port: config.catalog.port,
				headers:{
					Authorization: config.catalog.auth
				}
			};
		LOG.info(path);
		https.get(options, function(response){
			if (response.statusCode.toString()[0] !== "2" && response.statusCode !== 403){
				LOG.info(response.statusCode);
				requestDownloadFailed();
				return;
			}
			res.send(response.statusCode);
		}).on('error', function(err){
			LOG.info("Request Permission ERROR", err);
			requestDownloadFailed();
		});

		function requestDownloadFailed(){
			var errorMsg = "There was an issue requesting the download.";
			res.send({externalId: externalId, user: user, errorMsg: errorMsg}, 500);
		}
	}

		// req, res are EXPRESSJS objects
	function requestPermission(req, res){
		var source = req.param('source'),
			externalId = req.param('externalId'),
			reason = encodeURIComponent(req.param('reason')),
			user = req.header('policy-cn', helper.defaultUser()),
			path = "/package/original/request/" + source + "/" + externalId + "?user=" + user + "&reason=" + reason;
		var options = {
			host: config.catalog.host,
			path: path,
			port: config.catalog.port,
			method: 'POST',
			headers:{
				Authorization: config.catalog.auth
			}
		};
		LOG.info(path);
		https.request(options, function(response){ // this response is naturally just a native nodeJS/HTTP object, not expressjs

			for (var h in response.headers){
				var header = response.headers[h]
				LOG.info('HEADER: '+h+': '+header);
				// res.header(h, header); // dont copy headers right now. problems on higher lanes makes this un trustworthy
			}
			res.status(response.statusCode);
			res.contentType('text/xml');

			var myReply = "";

			response.on('data', function(chunk){
				myReply += chunk;
			});

			response.on('end', function(chunk){
				LOG.info(response.statusCode +": "+ myReply);
				// im done reading in the response!!!
				res.end(myReply);
			});

		}).on('error', function(err){
			LOG.info("Request Permission ERROR", err);
			permissionRequestFailed();
		}).end();
		function permissionRequestFailed(){
			var errorMsg = "Requesting permission for this asset failed.";
			res.send({externalId: externalId, user: user, errorMsg: errorMsg}, 500);
		}
	}

	function getRequestsForEmail(callback){
		var user = config.catalog.creditials.split(':')[0],
			path = "/package/original/getRequests?username=" + user + "&user=" + user + "&startDate=",
			daysAgo = new Date();
		daysAgo.setDate(daysAgo.getDate() - 30);
		var day = helper.addZero(daysAgo.getDate()),
			month = helper.addZero(daysAgo.getMonth() + 1),
			year = daysAgo.getFullYear();
		path += day + '-' + month + '-' + year;
		var options = {
			host: config.catalog.host,
			path: path,
			port: config.catalog.port,
			headers:{
				Authorization: config.catalog.auth
			}
		};
		LOG.info(path);
		https.request(options , function(delegateResponse){
			var parser = new xml2js.Parser();

			delegateResponse.on('data', function(chunk){
				parser.parseString(chunk);
			});

			parser.addListener('end', function(result){
				delegateResponse.on('end',function(){
					callback(null, _translate(result));
				});
			});
		}).on('error', function(e){
			callback(e, null);
		}).end();
	}

	function lookupAccount(lookup, callback){
		var user = config.catalog.creditials.split(':')[0],
			path = "/user/lookup?q=" + lookup + "&user=" + user;
		var options = {
			host: config.catalog.host,
			path: path,
			port: config.catalog.port,
			method: 'GET',
			headers:{
				Authorization: config.catalog.auth
			}
		};
		LOG.info(path);
		https.request(options, function(response){
			var statusCode = response.statusCode.toString();
			if(statusCode[0] !== "2"){//FAILURE
				LOG.info('User lookup Failed with status code: ' + statusCode);
				return callback({ "error": 'User lookup Failed with status code: ' + statusCode});
			}
			var parser = new xml2js.Parser();
			response.on('data', function(chunk){
				parser.parseString(chunk);
			});

			parser.addListener('end', function(result){
				response.on('end',function(){
					return callback(result);
				});
			});
		}).on('error', function(err){
			LOG.info('User lookup Failed with error: ' + err);
			return callback({ "error": 'User lookup Failed with error: ' + err});
		}).end();
	}

	function updateSentEmail(id){
		var user = config.catalog.creditials.split(':')[0],
			path = "/package/original/updateRequests?requestIds=" + id + "&acknowledged=true&actionTaken=true&user=" + user;
		var options = {
			host: config.catalog.host,
			path: path,
			port: config.catalog.port,
			method: 'POST',
			headers:{
				Authorization: config.catalog.auth
			}
		};
		LOG.info(path);
		https.request(options, function(response){
			var statusCode = response.statusCode.toString();
			if(statusCode[0] !== "2"){//FAILURE
				LOG.info('Updating emails sent Failed with status code: ' + statusCode);
			}
		}).on('error', function(err){
			LOG.info('Updating emails sent Failed with error: ' + err);
		}).end();
	}

	return {
		search: search,
		entry: entry,
		downloadHandler: downloadHandler,
		downloadRequests: downloadRequests,
		requestDownload: requestDownload,
		requestPermission: requestPermission,
		getRequestsForEmail: getRequestsForEmail,
		lookupAccount: lookupAccount,
		updateSentEmail: updateSentEmail
	};
});