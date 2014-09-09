define(["handlebars"], {
  rootDir: function(dir){
    this._rootDir = dir;
  },
  extension: function(ext){
    this._ext = ext;
  },
  load: function(name, req, load){
    var path = this._rootDir + name + this._ext;
    req(["text!" + path], function(tmpl){
      var renderFunc = Handlebars.compile(tmpl);
      load(renderFunc);
    });
  },
  _rootDir: "",
  _ext: ".html",
  _cache: {} //todo: impliment caching with this
});
