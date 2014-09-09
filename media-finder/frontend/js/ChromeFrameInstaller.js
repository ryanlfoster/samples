define(["tmpl!ChromeFrameInstaller", "js/chromeframe/CFInstall.min.js"], function(template) {
  return {
    install: function(){
      var elem = $(template());
      var target = $("div#search");
      var strip = target.find(".strip");
      strip.children().remove();
      strip.append(elem);
      target.addClass("chromeFrame");
      $(".install", elem).click(function(){
        target.remove();
        CFInstall.check({
          node: "placeholder",
          destination: '/'
        });
      });
    }
  };
});
