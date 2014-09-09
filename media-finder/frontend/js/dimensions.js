define(["text!../css/dimensions.less"], function(text){
  var dimensions = {};
  text.split("\n").forEach(function(line){
    var res = /^@(\w*):\s*(\d*)px;/.exec(line);
    if (res !== null && res.length === 3){
      dimensions[res[1]] = res[2];
    }
  });
  return dimensions;
});
