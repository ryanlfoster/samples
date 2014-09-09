define(["jquery"], function($){
  $.fn.fixed = function(){
    var top = this.offset().top;
    this.css({
      position: "fixed",
      top: top
    });
  };
});

if (!("bind" in Function.prototype)){
  Function.prototype.bind = function(){
    var args1 = Array.prototype.slice.call(arguments);
    var target = args1.shift();
    var method = this;
    return function(){
      var args2 = Array.prototype.slice.call(arguments);
      return method.apply(target, args1.concat(args2));
    }
  }
}
