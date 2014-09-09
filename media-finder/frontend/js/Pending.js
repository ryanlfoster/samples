define(["tmpl!Pending", "widget", "pubsub", "SearchService"],
function(template, widget, pubsub, Ajax){

  function Pending(selector, data){
    this._data = data;
    this._template = template;
    this._ajax = new Ajax();
    this._loadRequests();
    var self = this;
    setInterval(function(){
      if(self._elem.attr('data-changed')){
        self._loadRequests();
      }
    }, 1000);
    mixin(widget, pubsub, this).placeInDom(selector);
  }

  Pending.prototype._wireUp = function(elem, $$, self){
    $(document).on('click', function(e){
      if($(e.target).closest('#pendingRequests').length === 0){
        $$('.requestPanel').hide();
      }
    });
    $$('.icon').on('click', function(){
      $$('.requestPanel').toggle();
    });

    $$('.showAsset').on('click', function(e){
      var id = $(e.target).attr('data-assetId'),
          url = window.location.protocol + "//" + window.location.host + "/entry/" + id;
      var entryWindow = window.open(url, '_blank');
      entryWindow.focus();
    });

    $$('th[data-sort]').on('click', function(e){
      var target = $(this),
          column = target.attr('data-sort'),
          direction = target.attr('data-direction') || "ASC",
          table = target.closest('table'),
          rows = table.find('td[data-sort="'+ column +'"]');
      rows.sort(direction == "ASC" ? _sortTableDESC : _sortTableASC);
      table.find('thead [data-sort]').attr('data-direction', '');
      target.attr('data-direction', direction=="ASC" ? "DESC" : "ASC");
      table.find('tbody tr').remove();
      rows.each(function(){
        table.find('tbody').append($(this).parent());
      });
    });

    function _sortTableASC(a,b){
      var first = $(a).text(),
          second = $(b).text();
        
        if (!isNaN(first) && !isNaN(second)){
          //both numbers
          return (parseInt(first.replace(',',''), 10) >= parseInt(second.replace(',',''), 10)) ? 1 : -1;
        }else{
          //try for dates
          var firstDay = new Date(first),
            secondDay = new Date(second);
          
          if (!isNaN(firstDay) && !isNaN(secondDay)){
            return (+firstDay >= +secondDay) ? 1 : -1;
          }else{
            return (first >= second) ? 1 : -1;
          }
        }
    }

    function _sortTableDESC(a,b){
      var first = $(a).text(),
          second = $(b).text();
        
        if (!isNaN(first) && !isNaN(second)){
          //both numbers
          return (parseInt(first.replace(',',''), 10) >= parseInt(second.replace(',',''), 10)) ? -1 : 1;
        }else{
          //try for dates
          var firstDay = new Date(first),
            secondDay = new Date(second);
          
          if (!isNaN(firstDay) && !isNaN(secondDay)){
            return (+firstDay >= +secondDay) ? -1 : 1;
          }else{
            return (first >= second) ? -1 : 1;
          }
        }
    }
  };

  Pending.prototype._loadRequests = function(){
    var self = this;
    this._ajax.getRequestStatus(function(data){
      if(!$.isEmptyObject(data)){
        data.downloadRequest = data.downloadRequest instanceof Array ? data.downloadRequest : [data.downloadRequest];
      }
      self._data = data;
      self.render(self._data);
      $('th[data-sort="date"]').removeAttr('data-direction').click();
    });
  };

  return Pending;
});
