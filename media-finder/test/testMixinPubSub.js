define(["mixinPubSub"], function(mixinPubSub) {
  test("mixinPubSub exports correctly", function(){
    equals(typeof mixinPubSub, 'function', "PubSub should export a function");
  });

  test("mixinPubSub mixes in methods", function(){
    var obj = {}
    ok(!("on" in obj));
    ok(!("fire" in obj));
    mixinPubSub(obj);
    ok("on" in obj);
    ok("fire" in obj);
  });

  test("mixinPubSub fires events", function(){
    var obj = {},
        eventFired = false;
    mixinPubSub(obj);
    obj.on("event", function(){
      eventFired = true;
    });
    obj.fire("event");
    ok(eventFired);
  });

  test("mixinPubSub events isolated", function(){
    var obj = {},
        eventFired = false;
    mixinPubSub(obj);
    obj.on("event", function(){
      eventFired = true;
    });
    obj.fire("SomeOtherStupidEvent");
    ok(!eventFired);
  });

  test("mixinPubSub fire passes args to callbacks", function(){
    var obj = {},
        eventArg;
    mixinPubSub(obj);
    obj.on("event", function(arg){
      eventArg = arg;
    });
    obj.fire("event", 42);
    equals(eventArg, 42);
  });

  test("mixinPubSub fire passes multiple args to callbacks", function(){
    var obj = {},
        result;
    mixinPubSub(obj);
    obj.on("event", function(a, b){
      result = a+b;
    });
    obj.fire("event", 21, 32);
    equals(result, 53);
  });

});
