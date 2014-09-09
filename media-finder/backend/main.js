#!/usr/bin/env node

//start in the directory of *this* file, not in the
//  directory of where it is run from
process.chdir(__dirname);

var requirejs = require('requirejs');
requirejs(['xml2js', 'express', 'fs', './routes/home', './routes/ws', './lib/utils', './routes/collections', './lib/logger'],
function (  xml2js,   express,   fs,            home,            ws,         utils,            collections,         LOG){

	LOG.info(Date.now());

	var config = utils.setupConfig();
	var app = express.createServer();

	app.configure(function(){

		// enable gzip compression
		app.use(express.compress());

		app.use(express.bodyParser());
		//setup logging
		if (!config.skipLogging){
			var stream = fs.createWriteStream(config.accessLogPath, {flags:"a"});
			stream.on("error", function(e){
				LOG.info("couldn't open "+config.accessLogPath+" for logging");
				LOG.info("LOGS WILL NOT BE WRITTEN.");
				LOG.info(e);
			});
			app.use(express.logger({
				stream: stream
			}));
		}

		app.use(function(req, res, next){
			delete res._headers.vary
			next();
		});

		//serve static files
		app.use(express.static(__dirname + '/../frontend'));
		//TODO based on env variables (NODE_ENV = PRODUCTION | DEVELOPMENT | x) serve frontend-build
		app.use(express.errorHandler({ dumpExceptions: true, showStack: true }));
	});

	app.get('/', home.index);
	app.get('/canary', home.canary);
	app.get('/ping', home.ping);
	app.get('/headers', home.headers);
	app.get('/entry/:id', home.entry);
	app.get('/vpnerror', home.vpnError);
	app.get('/faq', home.faq);

	app.get('/ws/search', ws.search);
	app.get('/ws/entry/:id', ws.entry);
	app.get('/ws/download/:vId/:rendering', ws.downloadHandler);
	app.get('/ws/download/:vId', ws.downloadHandler);
	app.get('/ws/requests', ws.downloadRequests);
	app.get('/ws/ask/permission', ws.requestDownload);
	app.get('/ws/request/permission', ws.requestPermission);

	//I am aware that these break the convention that the app has set, but these are a little more REST compliant
	app.get('/ws/collections', collections.getAll);
	app.post('/ws/collections', collections.create);
	app.get('/ws/collections/:id', collections.get);
	app.put('/ws/collections/:id', collections.update);
	app.del('/ws/collections/:id', collections.destroy);
	app.get('/ws/collections/:id/entries', collections.getEntries);
	app.get('/ws/collections/:id/entries/search', collections.searchEntries);
	app.get('/ws/collections/:id/entries/search/:term', collections.searchEntries);
	app.post('/ws/collections/:id/entries', collections.addEntry);
	app.del('/ws/collections/:id/entries/:entryId', collections.destroyEntry);
	app.get('/ws/collections/:id/users', collections.getUsers);
	app.post('/ws/collections/:id/users', collections.addUser);
	app.del('/ws/collections/:id/users/:username', collections.destroyUser);
	app.get('/ws/users/search/:term', collections.userSearch);

	app.listen(3000);
	LOG.info("Server running. Listening on port 3000.");
	try {
		process.setuid("svc-cmnode");
	} catch (e) {
		LOG.info("WARNING: user not downgraded!");
		LOG.info(e);
	}

	setInterval(askForEmails, 3600000);//Every Hour
	function askForEmails(){
		ws.getRequestsForEmail(function(err, data){
			var requests = data.xml.downloadRequest;
			for(var i in requests){
				var r = requests[i],
					status = r.status || "Pending";
				if(typeof r.actionTaken === 'undefined' && status !== "Pending"){
					ws.lookupAccount(r.username, new sendTheEmail(r['@'].id, r.username, r.externalId, r.assetName, r.entryId, (r.status === "Approved")));
				}
			}
		});
	}
	var sendTheEmail = function(id, username, externalId, assetName, entryId, status){
		return function(result){
			if(result.user && result.user.ldsEmail){
				LOG.info('Sending email to: ' + result.user.ldsEmail);
				utils.sendEmail(result.user.ldsEmail, username, externalId, assetName, entryId, status, function(){
					ws.updateSentEmail(id);
				});
			}else{
				LOG.info('No email found for : ' + username);
			}
		};
	};
});