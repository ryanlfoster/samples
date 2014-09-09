({
	// Path to the project
	//appDir : "src/main/webapp",
	appDir : "{{appDir}}",

	// Path containing all JavaScript files (relative to appDir)
	baseUrl : "js",

	// Path to the build target (must be different than appDir)
	dir : "target/webapp-build",

	// Use this to get requirejs config so you don't have duplicate paths/shim/etc
	mainConfigFile : "js/requirejs-config.js", //(relative to the location of app.build.js)

	// Needed when you use nested require statements in order to have nested dependencies included in the optimized build 
	findNestedDependencies : true,

	// Use "none" to skip CSS optimization (removal of comments, white space, etc)
	optimizeCss : "standard",

	// Tells the build to spit out results as they happen instead of at the end
	logLevel : 0,

	optimize : "uglify", // Use "none" to combine but NOT compress (useful for debugging)

	// JavaScript modules to optimize.  All module dependencies will be included in the module's built file
	modules : [
		{ name : "main" }, // points to main.js
		{ name : "admin" } // points to admin.js
	],

	// Use this if you don't want to copy certain files/folders as part of the build.
	// This is a useful setting because by default the build will follow symlinks and copy ALL assets it finds,
	// taking a very long time if you have lots of pictures and videos.
	// This regex for example tells the build to ignore anything that starts with a . or is "videos" or "media".
	// fileExclusionRegExp: /^(\.|(videos|media)$)/,

	// Additional settings for openweb-build
	openweb : {

		//CSS folders to look in when doing @import in LESS
		styleDirs : [],

		//file extensions to evaluate for links to LESS files and less.js
		stripExtensions : ["html", "jsp"],

		// Symbolic links to create in the build directory at the end of the build.
		// On windows, this requires that the build be "Run as adminstrator".
		// This will create a symlink named "videos" that points to ../../videos (relative to the build dir).
		symlinks : {
			//videos : "../../videos", 
			//media : "../../media",
		}
	}
})