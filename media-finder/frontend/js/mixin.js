var mixin = (function(){
  function mixin(){
    var mixin, i, result, key
        mixins = Array.prototype.slice.call(arguments),
        target = mixins.pop();

    for (i=0, len=mixins.length; i<len; i++){
      mixin = mixins[i];
      if (typeof mixin === "object"){
        for (key in mixin){
          if (key in target){
            throw "Attempt to mixin property " + key + " but it already exists";
          } else {
            target[key] = deepCopy(mixin[key]);
          }
        }
      } else {
        throw "mixin: attempt to mixin variable of type: " + typeof mixin;
      }
    }
    return target;
  }

  function deepCopy(input){
    if (typeof input === 'object'){
      var result = {};
      for (var key in input){
        result[key] = deepCopy(input[key]);
      }
      return result;
    } else {
      return input;
    }
  };

  mixin.deepCopy = deepCopy;

  return mixin;
})();

