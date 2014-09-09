define([], function(){

  var POPUP_FADEIN_TIME = 300; //miliseconds
  var RESIZE_TIME = 200; //miliseconds
  var currentPopup = null;

  var fader = {
    fade: function(time){
      $("body").append(this._elem);
      $("body").css("overflow","hidden");
      this._elem.hide().fadeIn(time || 300);
      this._fadedUp = true;
    },
    remove: function(){
      var elem = this._elem;
      elem.fadeOut(POPUP_FADEIN_TIME, function(){ elem.detach(); });
      this._fadedUp = false;
      var top = $("body").css("top");
      $("body").css("overflow", "auto");
    },
    wireUp: function(){
      var self = this;
      this._elem.click(function(){
        if (currentPopup){currentPopup.destroy();}
      });
      return this;
    },
    _elem: $("<div class='fullScreenFader' />"),
    _fadedUp: false
  }.wireUp();

  $(window).bind('scroll', scrollKill);
  $("body > div#all > div#header").click(removeCurrent);

  function removeCurrent(){
    if (currentPopup){ currentPopup.destroy(); }
  }

  function scrollKill(){
    if (currentPopup && !currentPopup._noKillOnScroll){currentPopup.destroy();}
  }

  var popup = {
    destroy: function(){
      $(window).unbind('scroll', scrollKill);
      $(window).off('click');
      var self = this;

      if (self == currentPopup){
        fader.remove();
      }
      (this._onDestroy || []).forEach(function(action){
        action();
      });
      this._onDestroy = [];
      self._elem.remove();
    },

    onDestroy: function(action){
      this._onDestroy = this._onDestroy || [];
      this._onDestroy.push(action);
    },

    popup: function(position, multiple){

      this._elem.css({
        position: "fixed",
        "z-index": "100",
        top: position.top,
        left: position.left
      });

      this._elem.addClass("modal");

      if (!multiple && currentPopup !== this){
        removeCurrent();
        currentPopup = this;
      }

      this._elem.hide();
      this._elem.fadeIn(POPUP_FADEIN_TIME);
      setTimeout(function(){
        $(window).one('click', function(e){
          if($(e.target).closest('.modal').length === 0){
            $('.modal').hide();
          }
        });
      }, 0);
    },

    popupModal: function(){
      if (currentPopup !== this){
        removeCurrent();
        currentPopup = this;
      }

      this._noKillOnScroll = true;

      this._elem.addClass("modal");
      fader.fade(RESIZE_TIME);

      var width  = parseInt(this._elem.width(), 10);
      var height = parseInt(this._elem.height(), 10);
      var win = $(window);
      var winWidth  = win.width();
      var winHeight = win.height();
      this._elem.css({
        position: "fixed",
        "z-index": "100",
        top:    (winHeight - height) / 2 + "px",
        left:   (winWidth  - width)  / 2 + "px"
      }, RESIZE_TIME, "swing");
    },

    killCurrent: removeCurrent
  };


  return popup;
});
