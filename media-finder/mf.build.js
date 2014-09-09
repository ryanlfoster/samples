({
	mainConfigFile: 'frontend/js/main.js', // it will gather the PATHS from here
	baseUrl:	'frontend/js', // from here where are the MODULES?
	inlineText:	true,

	// single file optimization
	name:'main',
	include: ['main'],
	out: 'frontend/js/main.js'

})