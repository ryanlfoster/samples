define(["mixin"], function(mixin) {

  test("mixinPubSub exports correctly", function(){
    equals(typeof mixin, 'function', "mixin should export a function");
    equals(typeof mixin.deepCopy, 'function', "mixin should export a function");
  });

  test("deepCopy", function(){
    var original = {name:"bob", age: 107};
    var copy     = mixin.deepCopy(original);
    equals(typeof copy, 'object');
    deepEqual(copy, original, "the properties should be copied!");
    copy.name = "Mr. " + copy.name;
    equals(copy.name, "Mr. bob", "the copy should be changed");
    equals(original.name, "bob", "the copy should be changed");
  });
});
