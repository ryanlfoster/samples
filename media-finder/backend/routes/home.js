define(['fs', '../lib/utils'], function(fs, utils){
	var config = utils.setupConfig();
	function index(req, res){
		var path, fs = require('fs');
		path = '../frontend/mediaFinder.html';
		var stream = fs.createReadStream(path);
		stream.on('open', function(){
			res.header('Content-Type', "text/html");
		}).on('data', function(data){
			res.send(data);
		});
	}

	function canary(req, res){
		fs.readFile('buildargs.txt', 'utf8', function (err, data) {
			if (err) {
				res.send("error reading buildargs.txt: " + err, 500);
			} else {
				var parts = data.split(' ');
				if (parts.length !== 2){
					res.send("error parsing buildargs.txt, it contains: " + data, 500);
				} else {
					var result = "";
					var resultData = {
						laneName: parts[0],
						revision: parts[1],
						catalogUrl: config.catalog.host
					};
					for (var key in resultData) {
						result += '<tr><td>'+key+': </td><td>'+resultData[key]+'</td><tr>';
					}
					result = "<html><body><table>" + result + "</table></body></html>";
					res.send(result);
				}
			}
		});
	}

	function ping(req, res){
		res.send('UP');
	}

	function headers(req, res){
		res.json(req.headers);
	}

	function faq(req, res){
		var path, fs = require('fs');
		path = '../frontend/faq.html';
		var stream = fs.createReadStream(path);
		stream.on('open', function(){
			res.header('Content-Type', "text/html");
		}).on('data', function(data){
			res.send(data);
		});
	}

	function entry(req, res){
		var path, fs = require('fs');
		path = '../frontend/entryPage.html';
		var stream = fs.createReadStream(path);
		stream.on('open', function(){
			res.header('Content-Type', "text/html");
		}).on('data', function(data){
			res.send(data);
		});
	}

	function vpnError(req, res){
		var path, fs = require('fs');
		path = '../frontend/vpnError.html';
		var stream = fs.createReadStream(path);
		stream.on('open', function(){
			res.header('Content-Type', "text/html");
		}).on('data', function(data){
			res.send(data);
		});
	}

	return {
		index: index,
		canary: canary,
		ping: ping,
		headers: headers,
		entry: entry,
		vpnError: vpnError,
		faq: faq
	};
});