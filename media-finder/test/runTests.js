//list of test modules to run
require([
  'testPubSub',
  'testSearch',
  'testMixinPubSub',
  'testMixin',
]);

//make sure test modules' imports are reletive to our code
require({
  baseUrl: "../frontend/js",
  paths: {
    "text": "lib/text"
  },
}, ['lib/jquery']);
