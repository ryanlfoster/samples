define(["PubSub"], function(PubSub) {
  //todo: pull all of these pubsub tests out out into their own test module
  test("PubSub exports correctly", function(){
    equals(typeof PubSub, 'function', "PubSub should export a function");
    var pubsub = new PubSub();
    ok (pubsub instanceof PubSub, "PubSub function should be a constructor");
  });

  test("PubSub.publish", function(){
    var result = false,
        pubsub = new PubSub();
    pubsub.subscribe(function(){
      result = true;
    });
    pubsub.publish();
    ok(result, "publish should cause callback to set result to true");
  });

  test("PubSub.unsubscribe", function(){
    var result = true,
        pubsub = new PubSub();

    function callback(){
      result = false;
    }

    pubsub.subscribe(callback);
    pubsub.unsubscribe(callback);

    pubsub.publish();
    ok(result, "callback should not be called");
  });

  test("PubSub.unsubscribe one-time callback", function(){
    var result = true,
        pubsub = new PubSub();

    function callback(){
      result = false;
    }

    pubsub.once(callback);
    pubsub.unsubscribe(callback);

    pubsub.publish();
    ok(result, "callback should not be called");
  });

  test("PubSub.once", function(){
    var result = 0,
        pubsub = new PubSub();

    pubsub.once(function(){
      result += 1;
    });

    pubsub.publish();
    pubsub.publish();

    equals(result, 1, "'once' callbacks should only be called once");
  });
});
