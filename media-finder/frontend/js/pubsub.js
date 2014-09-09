define(function(){
  if(typeof Function.prototype.bind != 'function'){
    Function.prototype.bind = function (bind) {
        var self = this;
        return function () {
            var args = Array.prototype.slice.call(arguments);
            return self.apply(bind || null, args);
        };
    };
  }

  return {
    _events:{},

    on: function(event, cb, context){
      if (typeof event === "string"){
        (this._events[event] = this._events[event] || []).push(cb.bind(context || null));
      } else if (typeof event === "object"){
        for (var ev in event) {
          this.on(ev, event[ev], context || event);
        }
      }
    },

    fire: function(event){
      if (event === undefined){
        throw "pubsub: cannot fire 'undefined' as event";
      }
      var eventsList = this._events[event] || [],
          args = Array.prototype.slice.call(arguments, 1);
      for (var i=0, len=eventsList.length; i<len; i++){
        eventsList[i].apply(null, args);
      }
    }
  };
});
