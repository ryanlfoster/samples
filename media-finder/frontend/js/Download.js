define(["popup", "widget", "tmpl!Download", "omniture", "SearchService"],
function(popup, widget, template, omniture, Ajax){

  function Download(data, offset, isSubmodal){
    this._data = processData(data);
    mixin(popup, widget, this);
    this._template = template;
    this._isSubmodal = isSubmodal;
    this._offset = offset;
    this._ajax = new Ajax();

    this.placeInDom("body");
    this.popup(this.calculatePopupPosition(offset), isSubmodal);
  }

  Download.prototype._wireUp = function(elem, $$, self){
    self._ajax.onError(function(error){
      try{
        var err = $.parseJSON(error);
        window.displayErrorMsg(err.externalId, err.user, err.errorMsg);
      }catch(e){
        showMessage("<h2>Sorry.</h2><p>" + error + "</p>");
        self.popup(self.calculatePopupPosition(self._offset), self._isSubmodal);
      }
    });
    self._ajax.askForPermission(self._data.metadata.externalId, self._data.metadata.repositoryName, function(hasPermission){
      elem.removeClass('loader');
      if(!hasPermission){
        elem.addClass('denied');
        self.popup(self.calculatePopupPosition(self._offset), self._isSubmodal);
        $$('textarea').focus();
      }
    });

    $$('.requestPermission').click(function(e){
      var target = $(e.target),
          externalId = target.attr('data-external-id'),
          source = target.attr('data-source'),
          reason = target.closest('.ask').find('textarea').val();
      if($.trim(reason) === ""){
        $(".requestError").css('display', 'inline-block');
      }else{
        self._ajax.requestPermission(externalId, source, reason, function(){
          $('#pendingRequests').attr('data-changed', true);
          var msg = "<h2>Thank You.</h2><p>Your request has been successfully submitted</p>";
          showMessage(msg);
          self.popup(self.calculatePopupPosition(self._offset), self._isSubmodal);
        });
      }
    });

    $$(".close, .arrow").click(function(){
      self.destroy();
    });
    $$(".downloadRow").click(function(){
      var elem = $(this);
      var vId = elem.attr("data-versionId");
      var rendering = elem.attr("data-rendering");
      var id = elem.attr("data-id");
      omniture.reportDownload(id);
      window.open("/ws/download/"+vId+"/"+rendering, "_newtab").blur();
      window.focus();
    });
  };

  Download.prototype.calculatePopupPosition = function(offset){
    var leftbarWidth = 220;
    var dlWidth = this._elem.hasClass('denied') ? 502 : 302;
    var dlHeight = this._elem.hasClass('denied') ? 282: 202;
    var win = $(window);
    var scrollTop = win.scrollTop();
    var winWidth = win.width();
    var winHeight = win.height();
    var xPercent = (offset.left - leftbarWidth) / (winWidth - leftbarWidth);
    var result = {
      left: offset.left - xPercent * dlWidth,
      top: offset.top + 25 - scrollTop
    };

    if (result.top + dlHeight > winHeight){
      result.top -= dlHeight + 35;
      this.$$(".arrow")
        .removeClass('top')
        .addClass("bottom")
        .css({left: xPercent * dlWidth - 3});
    } else {
      this.$$(".arrow")
        .removeClass('bottom')
        .addClass("top")
        .css({left: xPercent * dlWidth - 6});
    }

    return result;
  };

  //used by the other window when a download fails
  window.displayErrorMsg = function(assetId, username, errorMsg){
    $('#download').removeClass('denied loader');
    var subject = "MediaFinder Download Failure";
    var email   = "HooperJM@ldschurch.org";
    var body    = "James Hooper,\n\n" +
                  "Catalog Asset # "+assetId+"\n\n" +
                  "This asset failed to download for MediaFinder user "+username+".\n\n" +
                  "<Please provide any other useful information here>";
    var mailto  = encodeURI("mailto:"+email+"?subject="+subject+"&body="+body);
    var msg = "<h2>We're Sorry.</h2>" +
          "<p>" + errorMsg + "</p>" +
          "<p>If the problem persists, please <a class=\"letusknow\" href=" +
          mailto + ">let us know.</a></p>";
    $("#download .downloadOptions").html('<div class="error">'+msg+'</div>');
  };

  function showMessage(msg){
    $("#download .downloadOptions").html('<div class="error">'+msg+'</div>');
    $("#download").removeClass('denied');
  }

  function processData(data){
    try {
      data.assets.asset = data.assets.asset.filter(function(asset){
        return asset.filename !== undefined;
      });
      var downloadableAssets = [];

      data.assets.asset.forEach(function(asset, index){
        asset.rendering = asset.versions.version.filter(function(version){
          return(version.type === "RENDERING");
        }).map(function(version){
          return version.qualifier;
        })[0] || "";
        if(asset.rendering !== "")
          downloadableAssets.push(asset);
      });
      data.assets.asset = downloadableAssets;
    } catch (e) {} //fail silently...
    return data;
  }

  Handlebars.registerHelper("downloadResolution", function(resField){
    if (typeof resField != "string"){ return "<unknown>"; }
    var match = /(\d*\s*[xX]\s*\d*)/.exec(resField);
    return (match === null) ? resField : match[1];
  });

  Handlebars.registerHelper("humanDataSizes", function(sizeField){
    if (typeof sizeField != "string"){ return ""; }
    var suffixes = ["B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];
    var byteSize = parseInt(sizeField, 10);
    var i = Math.floor(Math.log(byteSize) / Math.log(1000));
    var size = byteSize / (Math.pow(1000, i));
    return size.toFixed(size >= 100 ? 1 : 2) + " " + suffixes[i];
  });

  Handlebars.registerHelper("getFileExtension", function(fileField){
    var match = /\.(\w*)/.exec(fileField);
    return match ? match[1].toUpperCase() : "unknown";
  });


  return Download;
});
