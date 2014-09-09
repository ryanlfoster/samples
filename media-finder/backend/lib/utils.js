define(['https', 'xml2js', 'fs', 'nodemailer', './logger'],
function (https,  xml2js,   fs,   nodemailer, 		LOG){
	//todo: put this in its own module
	function delegate(req, res, options, translate){
		translate = translate || function(x){return x;};
		var sent = new Date();
		https.request(options , function(delegateResponse){
			var parser = new xml2js.Parser();
			delegateResponse.on('data', function(chunk){
				parser.parseString(chunk);
			});

			parser.addListener('end', function(result){
				delegateResponse.on('end',function(){
					res.json(translate(result), 200);
				});
			});
		}).on('error', function(e){
			res.send("delegation error: " + e.message, 500);
		}).end();
	}

	function urlParams(map){
		var paramArray = [];
		for (var key in map) {
			paramArray.push(key + "=" + map[key]);
		}
		return "?" + paramArray.join("&");
	}

	function setupConfig(){
		//read the config file
		var config = JSON.parse(fs.readFileSync("config.json"));
		
		//turn the authorization string into base64
		config.catalog.auth = 'Basic ' + new Buffer(config.catalog.creditials).toString('base64');

		return config;
	}

	function sendEmail(email, user, externalId, name, entryId, approved, callback){
		LOG.info(user);
		var config = setupConfig();
		var smtpTransport = nodemailer.createTransport("SMTP", {
			host: config.smtpServer
		});

		var mailOptions = {
			from: "MediaFinder <noreply@ldschurch.org>",
			to: email
		};

		if(approved){
			mailOptions.subject =  "Asset " + externalId + " - " + name + " approved for use";
			mailOptions.html = user + ", <br/>"+
				"<p>Asset " + externalId + " - " + name + " has been approved for the use you requested." +
				" You can <a href=https://mediafinder" + config.env + ".ldschurch.org/entry/" + entryId +
				">download the asset via Media Finder</a> any time during the next 30 days. <br/><br/> Thank you, <btr/>"+
				" MediaFinder Product Team <br/> https://mediafinder.ldschurch.org</p>";
		}else{
			mailOptions.subject = "Asset " + externalId + " - " + name + " Denied For Requested Use";
			mailOptions.html = user + ", <br/>" +
				" <p>The Intellectual Property Office has reviewed your request to use asset " + externalId + " - " + name + "." +
				" Unfortunately, the decision at this time is to decline use of the asset for the requested purpose." +
				" For more information about this decision, you may contact the Intellectual Property Office." +
				" <br/><br/> Thank you, <br/> MediaFinder Product Team<br/> https://mediafinder.ldschurch.org</p>";
		}

		smtpTransport.sendMail(mailOptions, function(err, response){
			if(err === null){
				LOG.info('EMAIL SUCCESS TO: ' + user + ' <'+email+'>');
				callback();
			}else{
				LOG.warn('EMAIL TO: ' + user + ' <'+email+'> FAILED WITH ERROR: ' + err);
			}
		});

	}


	var IP_CODE_GROUPS = {
		open: ["0","4","7","9"],
		approval: ["0E","0MR","1","1G","1AV","4MR","4AV","8","10"],
		restricted: ["5"]
	};

	return {
		delegate: delegate,
		urlParams: urlParams,
		setupConfig: setupConfig,
		IP_CODE_GROUPS: IP_CODE_GROUPS,
		sendEmail: sendEmail
	};
});