/*
 * This JS file is intent to replace some other global js files.
 * It adds namespace, so we will be sure there will no javascript variable name conflict.
 * 
 * It will eventually contain all our utility functions/objects. Please feel free to add your stuff. 
 * 
 */



/* ***************************************************************************
 * USAGE:  GathrNamespace (objectPath)
 * 
 * objectPath is a string separated by ".", please always begin with "Gathr."
 * 
 * For each page, It would be nice to have its only name space. 
 * For example, for compare packge page, what you can do is:
 *
 *     var findPackagePage = GathrNamespace("Gathr.ComparePackagePage");
 *
 *         It begins with "Gathr" oject, then your page specific object (in this case is "ComparePackagePage")
 *
 *              Now you have the ComparePackagePage object under Gathr, and it is used in your page only, 
 *              all your page global variables shall be put in this object.
 *              (for example, findPackagePage.myGlobalVar = 33;)
 *              
 * ************************************************************************** */
var GathrNamespace = function (objectPath) {
        if(!objectPath || typeof objectPath != "string") {
                alert("It's a bug.");
                return null;
        }
    var elementArr = objectPath.split('.'),
        parent = window,
        currentElement = '';    
    for(var i = 0; i < elementArr.length; i++) {
        currentElement = elementArr[i];
        parent[currentElement] = parent[currentElement] || {};
        parent = parent[currentElement];
        if(parent && (typeof parent != "object" || $.isArray(parent) )) {
                alert("It's also a bug.");
                return null;
        }
    }
    return parent;
};



/* ************************************************************************************
 * Please put all utility functions here except omniture, modal, survey etc, which will 
 * be in its separated namespace. When you use the utility functions, please keep the whole path,
 * 
 *              GathrNamespace("Gathr.UTIL").getFormattedPrice(...);
 * 
 * if you don't like using the whole path, you can use as your local variable.
 *              var myLocalUtil = GathrNamespace("Gathr.UTIL");
 *              myLocalUtil.getFormattedPrice(...);
 *              myLocalUtil.getQueryParam(...);
 * 
 * Method: getCookieOptions(option)
 *              This method is used as one of $.removeCookie() parameters, because, we need to
 * remove cookie at .gathr.com level. (some mBox stuff set cookie at .gathr.com).
 * *********************************************************************************** */
_.extend(GathrNamespace("Gathr.UTIL"), {
        getFormattedPrice: function(price){
                try {
                    var displayPrice = parseInt(price);
                    if(displayPrice==price)
                        price = displayPrice;
                    else
                        price = parseFloat(price).toFixed(2);
                } catch (e) {;}
                
                return price;
        },
        getQueryParam: function(name){
                if(typeof name !== 'string')
                        return "";
                name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
                var regexS = "[\\?&]" + name + "=([^&#]*)";
                var regex = new RegExp(regexS);
                var results = regex.exec(window.location.search);
                if(results == null)
                        return "";
                return decodeURIComponent(results[1].replace(/\+/g, " "));      
        },
        getEnv: function(){
                var url = (window.location+"").toLowerCase();
                if(url.indexOf("qa.gathr.com") > -1 || url.indexOf("qh.gathr.com") > -1)
                        return "q";
                if(url.indexOf("gathr.com") > -1 || url.indexOf("gathr-stage.aol.com") > -1)
                        return "p";
                return "d";
        },
        getCookieOptions: function(option) {
                var url = (window.location+"").toLowerCase();
                option = option || {};
                if(url.indexOf(".gathr.com")>-1)
                        return $.extend({path: '/', domain: '.gathr.com'}, option);
                return $.extend({path: '/'}, option);
        },
        normalizeVariable: function(v) {
                if(!v)
                        return v;
                return v.replace(/\-/g, "_");
        }
});


/* *******************************************************************************************
 *  how to setup product detail modal in your page:
 *  1, you need to include modal-product-detail.vm file (will remove this step later on)
 *  2, call: 
 *      var yourLocalVar = GathrNamespace("Gathr.MODAL").getProductDetailController();
 *              yourLocalVar.renderShow(productId, withReview, modalAlreadyOpen, callback, context);
 *  that's it!
 *     -- withReview(required), if it is true, automatically move to review section
 *     -- modalAlreadyOpen(required), if it is true, program assumes the modal window is current open
 *     -- callback(optional), a function you define, for updating some contents in the lightbox
 *     -- context(optional), the context for callback function.
 *     
 * **************************************************************************************** */
_.extend(GathrNamespace("Gathr.MODAL"), {
        private: {
                ProductDetailController: Backbone.View.extend({
                        initialize: function(){
                                /*
                                var modalHtml = '' +
                                        '<div id="productDetailModal_new" class="modal hide fade">' +
                                                '<div class="modal-header">' +
                                                        '<button class="close" data-dismiss="modal" aria-hidden="true"></button>' +
                                                '</div>'+
                                                '<div class="modal-body"></div>'+
                                        '</div>';
                                $("body").append(modalHtml);
                                this.setElement("#productDetailModal_new");
                                */
                        }, 
                        el: $("#productDetailModal"),
                        renderShow: function(productId, withReview, modalAlreadyOpen, callback, context){
                                if(!modalAlreadyOpen)
                                        this.$el.modal("show");
                                
                        this.loadProductDetail(productId, withReview, callback, context);
                        return this;
                        },
                        renderHide: function(){          
                        this.$el.modal("hide");
                    },
                    loadProductDetail: function(productId, withReview, callback, context){
                        var htmlStr = this[productId];                  
                        if(htmlStr) {
                                this.$(".modal-body").html(htmlStr);
                                
                                if(callback) {
                                        if(context)
                                                callback.call(context);
                                        else
                                                callback();
                                }
                                
                                if(withReview) { 
                                        setTimeout(function(){
                                                $('.collapsible-header', '#reviews').click();
                                                var jj = $("#productDetails > .row-fluid").get(0); 
                                                $("#productDetails").animate({ scrollTop: $(jj).height() }, 'slow');
                                        }, 400);
                                }
                                
                                //below several lines of code are copied from scripts.js file
                                //absolutely needs to cleanup, needs to redesign

                                getProductVideoData();
                                var productData = $('#productData').data();
                                        var reviewListView = new ReviewListView(productData.sku, EventDispatcher);
                                        reviewListView.render();
                                        /*
                                        var productDetailVideoModalContent = $('#productDetailVideoModal .modal-body');
                                        if(productData.videoid && productData.videoid !== '${product.videoId}'){
                                                populate5minScriptTag(productData.videoid, productDetailVideoModalContent[0], true,false);
                                        }
                                        */                      
                                        
          
              // Purely aesthetic, forces the product detail area to use the maximum available height
              // flushing the scrollbar to cover 100% of its height
          setTimeout(function(){
              var productBody = $("#productModal .product-detail-body"),
                  productModalHeight = parseFloat($('#productDetailModal').css('height')),
                  productBodyHeight = productModalHeight - productBody.position().top;
              if(productBodyHeight > 0 && productBodyHeight !== productModalHeight) {
                productBody.height(productBodyHeight);  
              }          
          }, 300);

                                return;
                        }

                        var containingObj = this;
                        $.ajax({
                                        type: 'GET'
                                    , url: "/product/modal/"+ productId
                                    , cache: false
                                    , contentType: 'text/html'
                                    , success: function(data, response, jqXHR){
                                        containingObj[productId] = data;
                                        containingObj.loadProductDetail(productId, withReview, callback, context); 
                                    }
                                        ,error: function(collection, xhr, options){
                                    alert("error"); 
                                }
                                });                     
                        return this;
                    }
                }),
                productDetailControllerInstance: null
        },
        getProductDetailController: function(){
                if(!this.private.productDetailControllerInstance)
                        this.private.productDetailControllerInstance = new this.private.ProductDetailController();
                return this.private.productDetailControllerInstance;            
        }       
});



/* ***************************************************************
 *  OMNITURE
 *  
 *  init(): this function should only be called in runOmni() funtion.
 *  runOmni() function is a global function defined by omniture.
 *  
 *  writeAuth(loginId): this function will be triggered on every page load.
 *  Whenever open auth responses, this function gets called.
 *  
 *  write(setting): this function is called on demand. "setting" is a
 *  JavaScript object, it will be used to overwrite the omniture variables.
 *  The field names in "setting" object must be the same as omniture variable 
 *  names.
 *  
 * ************************************************************* */
_.extend(GathrNamespace("Gathr.OMNITURE"), {
        skipOmnTrack: false,
        loginId: null,
        init: function(){
                if(typeof s_265=="undefined" || !s_265)
                        return;
                
                //i am not sure where the below logic comes from, maybe need to confirm with omniture team
                var listgridCC = $.cookie('list-grid');
            if(s_265.pageName == "checkout" || s_265.pageName == "download" ||
                        (s_265.pageName == "create your own bundle main" && listgridCC == 'list') ||
                        (s_265.pageName == "products main" && listgridCC == 'list'))
              this.skipOmnTrack = true;
        },
        write: function(setting){
                if(typeof s_265=="undefined" || !s_265 || typeof setting != "object")
                        return;
                _.extend(s_265, setting);
                if(this.loginId)
                        s_265.authOverride = true;
                else    
                        s_265.authOverride = false;
        
                if(s_265.pageName)
                        s_265.t();              
        },
        writeAuth: function(loginId) {
                var sns_rsp = "sn=";
                if(loginId) {
                        sns_rsp = "sn=" + $.base64.encode(encodeURIComponent(loginId));
                        $.cookie("RSP_COOKIE", sns_rsp, {path: "/" });
                        this.loginId = loginId;
                } else 
                        $.cookie("RSP_COOKIE", sns_rsp, {path: "/" });
                
                if(typeof s_265=="undefined" || !s_265 ||this.skipOmnTrack)
                        return;
                                
                if(loginId) 
                        s_265.authOverride = true;
                else 
                        s_265.authOverride = false;                     
                s_265.t();          
        },
        omniCustLink:function(linkName, eventNum, productName, type){
                var co = {};

            //Custom Link tracking only for local report suite
            co.un = s_265_account.split(',')[0];
            
            co.linkTrackVars = 'eVar5,eVar6';
            co.linkTrackEvents = 'event3,event4';
            
            if (!isNaN(eventNum)){
                co.events = 'event' + eventNum;
                co.linkTrackVars += ',events';
            }
            
            co.eVar5 = productName.toLowerCase();
            co.eVar6 = type.toLowerCase();

            //send the custom link request
            s_265.tl(true,'o', linkName, co);
        }

});




/* ****************************************************************************
 * OPEN AUTH
 * 
 * for open auth,  you dont need to do anything unless you want to register
 * some events.
 * 1. authLoggedIn (SNS signed in)
 * 2. authLoggedOff (SNS signed off, or anonymous user)
 * 3. authSessionCreated (after SNS signed in, session created event)
 * 4. authSessionDestroyed (after SNS signed off, session destroyed event)
 * 
 * Usage:
 *              var authStatusObj = GathrNamespace("Gathr.AUTH").getAuthStatus();
 *              authStatusObj.on("change:authLoggedIn", yourCallBackFunc, context);     
 * 
 * if you want to get user object, now you don't need to call /session/user,
 * just simply call:(assume the user already passed SNS and session created.)
 *              authStatusObj.get("user");
 * or
 *              GathrNamespace("Gathr.AUTH").getUser();
 *  
 * ************************************************************************** */ 
_.extend(GathrNamespace("Gathr.AUTH"), {
        getAuthStatus: function(){
                if(!this.private.authStatusInstance)
                        this.private.authStatusInstance = new this.private.authStatus();
                return this.private.authStatusInstance;         
        },
        getUser: function(){
                return this.getAuthStatus().get("user");
        },
        processAuth: function(){
                if( $('#aol-global-signin').length<1)
                        return;
                this.private.authControllerInstance = new this.private.authController();
                var protocol = window.location.protocol;
            var port = window.location.port;
            var location = protocol + "//" + window.location.hostname;
            if (protocol === "http:" && port !== 80) 
                location += ":" + port;
            else if (protocol === "https:" && port != 443) 
                location += ":" + port;
            location += "/_uac/authReceiver.html";
            $('#aol-global-signin').multiAuth({
                        devId:$("#authDevId").val(),
                        successUrl: location,
                        tabs:['aol'],
                        authServer: $("#authBaseUri").val(),
                        reload: 0,
                        getTokenCallback: this.authCallback
                });
        },
        authCallback: function (json) {
                var response = json.response;
                var authStatusObj =  GathrNamespace("Gathr.AUTH").getAuthStatus();
                if (response.statusCode === 200) {
                        var userAttr = response.data.userData.attributes;
                        var token = response.data.token;
                        var dataObj = {userData: userAttr, token: token};
                        
                        authStatusObj.set({userAttr: userAttr, token: token});
                        authStatusObj.attributes.authLoggedOff = false;
                        authStatusObj.set("authLoggedIn", true);
                        
                        new Backbone.Model().fetch({
                                type: 'POST',
                                url: '/session/internal',
                                contentType: 'application/json',
                                data: JSON.stringify(dataObj),
                                success: function(m){
                                        var user = m.toJSON();
                                        authStatusObj.set("user", user);
                                        authStatusObj.attributes.authSessionDestroyed = false;
                                        authStatusObj.set("authSessionCreated", true);
                                },
                                error: function(jqXHR, textStatus, errorThrown) {
                                        authStatusObj.set("authSessionError", textStatus);
                        }
                        });
                        
                        GathrNamespace("Gathr.OMNITURE").writeAuth(userAttr.loginId);
                        return;
                } 
                
                // reach here, means it is an anonymous user or user logged off
                authStatusObj.attributes.authLoggedIn = false;
                authStatusObj.set("authLoggedOff", true);
                
                new Backbone.Model().fetch({
                        type: 'GET',
                        cache: false,
                        url: '/session/logout',
                        contentType: 'application/json',
                        success: function(m){
                                authStatusObj.set("user", {});
                                authStatusObj.unset("authSessionError");
                                authStatusObj.attributes.authSessionCreated = false;
                                authStatusObj.attributes.authSessionDestroyed = false;
                                authStatusObj.set("authSessionDestroyed", true); 
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                                
                }
                }); 
                
                //dirty fix for dashboard server... When user logs off from microsite, we force logoff dashboard also
                if(typeof(sessionStorage)=="undefined" ||  sessionStorage.signoutJustClicked=="clicked") {
                        var dashboardLogout = "https://dashboard.gathr.com/aol/logout.php";
                        if(GathrNamespace("Gathr.UTIL").getEnv() != "p")
                                dashboardLogout = "https://gathr.lastpass.com/aol/logout.php";
                        $('<iframe>', {
                            css: {height:"1px", width: "1px", display: "none"},
                            src: dashboardLogout
                        }).appendTo('body');
                        if(typeof(sessionStorage)!="undefined")
                                sessionStorage.signoutJustClicked=="";                  
                }
                GathrNamespace("Gathr.OMNITURE").writeAuth();
        },
        private: {
                authController: Backbone.View.extend({
                        initialize: function(){
                                var authStatusObj = GathrNamespace("Gathr.AUTH").getAuthStatus();
                                authStatusObj.on("change:authLoggedIn", this.render, this);
                                authStatusObj.on("change:authLoggedOff", this.render, this);
                        },
                        el: "#openAuthDropdown ul.header-user-dropdown li.dropdown",
                        events: {
                                "click #global-signin": "authClick",
                                "click #top-sign-out": "authClick",
                                "click #top-access-dashboard": "mystuffClicked",
                                "touchend #global-welcome": "welcomeTouched"
                        },
                        authClick: function(e) {
                                this.$('#aol-global-signin').trigger('click');
                                //dirty fix for dashboard server... When user logs off from microsite, we force logoff dashboard also
                                if(typeof(sessionStorage)!="undefined" && $(e.currentTarget).attr("id")=="top-sign-out") 
                                        sessionStorage.signoutJustClicked = "clicked";
                                return false;
                        },
                        welcomeTouched: function(e) {

                                if($('html').hasClass("no-touch")==false) {
                                        e.stopPropagation();
                                        return false;
                                }       
                        },
                        mystuffClicked: function(){
                                var authStatusObj = GathrNamespace("Gathr.AUTH").getAuthStatus();
                                if(authStatusObj.get("authLoggedIn") == true){
                                        window.location = "/mystuff";
                                        return false;
                                }
                                this.$('#aol-global-signin').trigger('click');
                                this.mystuffClickedFlag = true;
                        },
                        render: function(){
                                var authStatusObj = GathrNamespace("Gathr.AUTH").getAuthStatus();
                                $('#nav-access-dashboard').parent().show();
                                if(authStatusObj.get("authLoggedIn")==true){
                                        if(this.mystuffClickedFlag){
                                                window.location = "/mystuff";
                                                return false;
                                        }
                                                
                                        this.$('#global-signin').hide(); 
                                        var userAgent = navigator.userAgent.toLowerCase();
                                        if (userAgent.indexOf('android') != -1) {
                                                this.$('#global-welcome').html('<i>Welcome ' + authStatusObj.get("userAttr").displayName + '!</i> &#187;').show();
                                                this.$('#global-welcome i').css('text-overflow','clip');
                                        } else
                                                this.$('#global-welcome').html('<i>Welcome ' + authStatusObj.get("userAttr").displayName + '!</i> &#x25BC;').show();
                                        $('#top-access-dashboard').parent().hide();
                                                                                
                                        return false;
                                }
                                
                                $('#global-welcome').hide();
                                $('#global-signin').show();
                                $('#top-access-dashboard').parent().hide();
                                return false;
                        }
                }),
                authStatus: Backbone.Model.extend({
                        defaults: {
                                user: {}
                    }
                }),
                authControllerInstance: null,
                authStatusInstance: null
        }
});





/* ****************************************************************************
 * SURVEY
 *  
 * ************************************************************************** */
_.extend(GathrNamespace("Gathr.SURVEY"), {
        enableSurvey: false,            // turn survey on/off
        selectPercentage: 20,
        idleTime:  15000,    //15000 means 15 seconds
        cookieDays: 7,                  // change to -1, means never set cookie
        surveyBaseUrl: "http://surveys.gathr.com/survey/",
        registerSurvey: function(){
                if(!this.enableSurvey)
                return;
                var surveyCookie = $.cookie('extsvy');
            if(surveyCookie)
                return;    
                var rand_no = Math.ceil(100*Math.random());              
            if(rand_no >= this.selectPercentage)
                return;
                var surveyPages = ["#howGathrWorks#home#products#findPackage#","#package#", "#customPackages#"]; //bodyId
                var bodyId =  $("body").attr("id");              
                var surveyType = -1;
                for(var i=0; i<surveyPages.length; i++) {
                        if(surveyPages[i].indexOf("#"+bodyId+"#")>-1) {
                                surveyType = i;
                                break;
                        }
                }
                if(surveyType < 0)
                        return;
                setTimeout(function(){
                        GathrNamespace("Gathr.SURVEY").lightBoxSurvey(surveyType);
                }, this.idleTime);
        },
        lightBoxSurvey: function(surveyType){ 
                var productailModal = $("#productDetailModal").css("display");
                var videoModal = $("#productDetailVideoModal").css("display");
                if( (productailModal && productailModal!="none") || (videoModal && videoModal!="none"))
                        return;
                var surveyLigthboxHtml = '' +
                        '<div id="surveyLightBox" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'+
                                '<div class="modal-header">'+
                                        '<button class="close" data-dismiss="modal" aria-hidden="true"></button>'+
                                        '<img src="https://s.aolcdn.com/os/gtr/prod/img/logos/gathr-logo.png" border="0" class="logo-gathr" />'+
                                        '<img src="https://s.aolcdn.com/bill.aol.com/new/images/40x40-Black-0.00.gif" class="logo-aol"/>'+
                                '</div>'+
                                '<div class="modal-body">'+
                                        '<h4>ONLINE SURVEY REQUEST</h4>'+
                                        '<p>FIRST IMPRESSIONS MATTER.<br/>'+
                                        'TAKE A SHORT SURVEY AND TELL US HOW WE ARE DOING.</p>'+
                                '</div>'+
                                '<div class="modal-footer">'+
                                        '<button class="btn" data-dismiss="modal" aria-hidden="true">NO THANKS. NOT NOW</button>'+
                                        '<button class="btn btn-primary">YES, I\'LL HELP</button>'+
                                '</div>'+
                        '</div>';
                $("body").append(surveyLigthboxHtml);           
                $('#surveyLightBox').modal('show');
                $('#surveyLightBox .modal-footer .btn-primary').click(function() {
                        $('#surveyLightBox').modal('hide');                             
                        GathrNamespace("Gathr.SURVEY").popupSurvey(surveyType);
                });
                
                $.cookie("extsvy", "gathr_0"+surveyType, { expires: this.cookieDays, path: "/" });
                if(typeof s_265!="undefined" && typeof s_265_account != "undefined" && s_265 && s_265_account) {
                        s_265.tl(true, 'o', 'gathr survey display', {un: s_265_account.split(',')[1]});
                }
        },
        popupSurvey: function(surveyType) {
                var baseurl = "";        
                switch(surveyType){
                case 1:
                        baseurl = this.surveyBaseUrl + "gathr_01";
                        break;
                case 2:
                        baseurl = this.surveyBaseUrl + "gathr_02";  
                        break;
                default:
                        baseurl = this.surveyBaseUrl + "gathr_00";  
                }
                
                var t = new Date();
                var tStr="";
                tStr += t.getUTCFullYear()+"";
                var monthV = t.getUTCMonth()+1;
                if(monthV>9)            
                        tStr += monthV+"";
                else
                        tStr += "0" + monthV;
                var dateV = t.getUTCDate();
                if(monthV>9)
                        tStr += dateV+"";
                else 
                        tStr += "0" + dateV;
                var hourV = t.getUTCHours();
                if(hourV>9)
                        tStr += hourV+"";
                else
                        tStr += "0" + hourV;
                var minuteV = t.getUTCMinutes();
                if(minuteV>9)
                        tStr += minuteV+"";
                else
                        tStr += "0" + minuteV;
                var secondV = t.getUTCSeconds();
                if(secondV>9)
                        tStr += secondV+"";
                else
                        tStr += "0" + secondV;
                var surl = baseurl + "&pagename="+encodeURIComponent(s_265.pageName)+"&type=s&rid="+tStr;
                window.open(surl,"survey","width=800,height=850,scrollbars=1");
                if(typeof s_265!="undefined" && typeof s_265_account != "undefined" && s_265 && s_265_account) 
                        s_265.tl(true, 'o', 'gathr survey', {un: s_265_account.split(',')[1]});
        }
});



/* ***************************************************************
 *  CART
 *  
 *  if you don't want to display cart on a specific page, please
 *  add it to "NoCartPage".
 *  
 * ************************************************************* */
_.extend(GathrNamespace("Gathr.CART"), {
        NoCartPage: ["/checkout", "/freemium", "/download", "/mystuff"],
        init: function(){
                var urlStr = window.location+"";
                for(var i=0; i< this.NoCartPage.length; i++) {
                        if(urlStr.indexOf(this.NoCartPage[i])>-1)
                                return;
                }
                new this.CartInfo();
        }, 
        CartInfo: Backbone.View.extend({
            initialize: function(){
                if(!$.cookie("cart"))
                        return;
                this.cartLoaded = false;   
                this.cartCookie = JSON.parse($.cookie("cart"));
                if(this.cartCookie.sku && this.cartCookie.sku!="lend" && this.cartCookie.sku !="freemium")
                        this.$el.removeClass('hidden');
            },
            el: "#yourOrderCart",
            events: {
                "click #yourOrderCartButton": "showModalCart",
                "click #yourOrderCartDropDown": function(e){e.stopPropagation();},
                "click #yourOrderCartDropDown .editButton": function(){window.location = "/custom-packages";return false;},
                "click #yourOrderCartDropDown .checkoutButton": function(){window.location = "/checkout";return false;},
                "click #yourOrderCartDropDown .removeButton": "clearCart"
            },
            showModalCart: function() {
                if($('html').hasClass("no-touch")==false) 
                                e.stopPropagation();
                        if(!this.cartLoaded) {
                                if(this.cartCookie.sku === 'custom')  
                                        this.loadCustomBundle();
                                else
                                        this.loadBundle();
                                this.cartLoaded = true;
                        }
                $('#yourOrderCartDropDown').show();
                $("body").one("click.cart_header", function(){
                        $('#yourOrderCartDropDown').hide();
                        }
                );
            },
            loadCustomBundle:function () {
                var customSelectedProducts = {productCollectionSkus: this.cartCookie.productIds, bonusProductCollectionSkus: this.cartCookie.bonusProductIds}; 
                new Backbone.Model().fetch({
                    type: "POST",
                    url: "/rest/bundle/cart/custom",
                    contentType: "application/json" ,
                    data: JSON.stringify(customSelectedProducts),
                    dataType: "json",
                    success: function (m){ 
                        new (GathrNamespace("Gathr.CART").CartDropDown)({bundle: m.toJSON(), isCustom: true}).render();
                    }
                });
            }
        ,   loadBundle: function(){
                new Backbone.Model().fetch({
                    type:"GET",
                    url:"/rest/bundle/cart/" + this.cartCookie.sku,
                    contentType:"application/json",
                    dataType:"json",
                    success:function (m) {
                        new (GathrNamespace("Gathr.CART").CartDropDown)({bundle: m.toJSON(), isCustom: false}).render();
                    }
                });
            },
            clearCart: function(event){ 
                $.removeCookie("cart", {path: "/" });
                this.$el.addClass('hidden');
                $('#yourOrderCartDropDown').hide();
                new Backbone.Model().fetch({
                    type: "DELETE",
                    url: "/session/cart",
                    contentType: "application/json" ,
                    dataType: "json"
                });
            } 
        }),              
        CartDropDown: Backbone.View.extend({
                el: "#yourOrderCartDropDown",
                render: function(){
                        this.$("li div.your-order-title h3").html(this.options.bundle.title);
                        if(!this.options.isCustom)
                                this.$("li div.your-order-title button").remove();
                        this.$("li div.your-order-title p").html(this.options.bundle.shortDescription);
                        if(this.options.bundle.discount)
                                this.$("li div.your-order-title p").after('<h4><strong>Your total savings: '+ this.options.bundle.discount +' per month!</strong></h4>');
                        this.$("a.mini-banner").attr({href: "/package/"+this.options.bundle.sku}).css({backgroundImage: "url('" + this.options.bundle.tileImage + "')"});
                        
                        this.$("div.your-order-2col ul").html(this.renderProductName());
                        this.$("div.your-order-3col ul").html(this.renderAffilicate());
                        
                        this.$("div.your-order-total").append("Your total: $"+ GathrNamespace("Gathr.UTIL").getFormattedPrice(this.options.bundle.price));
                        return;      
                },
                renderProductName: function(){
                var stringToReturn = "";
                for(var i = 0; i < this.options.bundle.products.length; i++){
                    var product = this.options.bundle.products[i];
                    if (i != this.options.bundle.products.length-1) 
                        stringToReturn += '<li>' + product.name + '</li><li class="spacer">&#8226;</li>';
                    else 
                        stringToReturn += '<li>' + product.name + '</li>';
                } 
                if(this.options.bundle.bonusProducts) {
                        for (var i = 0; i < this.options.bundle.bonusProducts.length; ++i) {
                                var item = this.options.bundle.bonusProducts[i];
                                stringToReturn += '<li class="spacer">&#8226;</li><li>' + item.name + '</li>';                          
                        }
                }
                return stringToReturn;
            },
                renderAffilicate: function(){
                        var stringToReturn = "";
                    for(var i = 0; i < this.options.bundle.affiliateProducts.length; i++){
                        var product = this.options.bundle.affiliateProducts[i];
                        var productName = product.name.replace(/\b(.net|.com)\b/gi, '');
                        stringToReturn += ''+
                                '<li>'+
                                        '<div class="icon">'+
                                                '<img src="' + product.smallIcon + '" alt="' + productName + '" />'+
                                        '</div>'+
                                        productName +
                                '</li>';
                    }
                    return stringToReturn;
                }
        })
});



$(function(){
        GathrNamespace("Gathr.SURVEY").registerSurvey();
        GathrNamespace("Gathr.AUTH").processAuth();
        GathrNamespace("Gathr.CART").init();
        $("footer div.footer-copy span:last").hide();
        //temporarily put it here, will move later. It is a bug in prod/qa now
        /*
        $('#yourOrderCartButton').on('touchstart', function(e) {
                if($('html').hasClass("no-touch")==false) {
                        e.stopPropagation();
                        return false;
                }
        });
        */
        
        $('body').on('touchstart.dropdown', '.dropdown-menu', function (e) { e.stopPropagation(); });
        
});