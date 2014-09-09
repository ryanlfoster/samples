define(['winston'],
	function (winston) {

		return new (winston.Logger)({
			transports:[
				new (winston.transports.Console)({timestamp:true}),
			]
		});

	});