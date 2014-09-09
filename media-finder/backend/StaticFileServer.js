define(['url', 'fs'], function(url, fs){
  function StaticFileServer(basePath, defaultMimeType){
    this._base = basePath;
    this._def = defaultMimeType;
  }

  StaticFileServer.prototype.serve = function(req, res){
    var path = this._base + url.parse(req.url).pathname,
        mime = this._getMimeType(path);
    fs.stat(path, function(err, status){
      if (err || !status.isFile()){
        //todo: put in a "soft 404" here 
        //      or just redirect to the right url
        res.writeHead(404);
        res.end();
        return;
      }
      var stream = fs.createReadStream(path);
      stream.on('open', function(){
        res.writeHead(200, {'Content-Type': mime});
      }).on('data', function(data){
        res.write(data);
      }).on('end', function(){
        res.end();
      });
      //todo: handle on('error'... here as well
    });
  }

  StaticFileServer.prototype._getMimeType = function(fname){
    var def = this._def || 'text/plain';
    return mimes[(/\.([^\.]*)$/(fname) || [])[1]] || def;
  };

  var mimes = {
    html: 'text/html',
    css: 'text/css',
    js: 'application/x-javascript',
    //todo: add image mime types here too
  };

  return StaticFileServer;
});
