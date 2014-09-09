define(["widget", "pubsub", "ControlWidget", "tmpl!IpGroups", "IpInfo"],
function(widget, pubsub, controlWidget, template, IpInfo){
  function IpGroups(selector, data){
    this._data = data;
    this._template = template;

    mixin(widget, pubsub, controlWidget, this);

    this.placeInDom(selector);
    this.setIpGroups(["open"]);
  }

  IpGroups.prototype.onSelectGroups = function(callback){
    this.on("selectGroups", callback);
  };

  IpGroups.prototype.setIpGroups = function(groups){
    var self = this;
    if (!groups.length){
      groups = ["open"];
    }
    this._deselectAll();
    groups.forEach(function(group){
      self.$$('.groupCheckbox[data-groupName="'+group+'"]').prop("checked", true);
    });
  };

  IpGroups.prototype._deselectAll = function(){
    this.$$('.groupCheckbox:checked').prop("checked", false);
  };

  IpGroups.prototype._wireUp = function(elem, $$, self){
    var arrow = $$('.arrow'),
        header = arrow.closest('h3');

    //pop up the large info panel
    $$(".info").click(function(event){
      var offset;
      if (self._infoPanel){
        self._infoPanel.destroy();
      } else {
        offset = $(this).offset();
        offset.left += 25;
        offset.top -= $(document).scrollTop();
        self._infoPanel = new IpInfo(offset);
        self._infoPanel.onDestroy(function(){
          self._infoPanel = null;
        });
      }
      event.preventDefault();
    });
    header.click(function(){
      if(arrow.hasClass('down')){
        self.collapse();
      }else{
        self.expand();
      }
    });
    $$(".groupCheckbox").change(function(){
      var result = [];
      if ($$(".groupCheckbox:checked").length === 0){
        $$('.groupCheckbox[data-groupName="open"]').prop("checked", true);
      }
      $$(".groupCheckbox:checked").each(function(){
        result.push($(this).attr("data-groupName"));
      });
      self.fire("selectGroups", result);
    });
  };

  return IpGroups;
});
