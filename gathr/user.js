// ================
// USER
// ================
// Summary


// DEFINITIONS
// ------------
// Term:            Definition


// DEPENDENCIES
// ------------
//  -   Backbone
//  -   jQuery
//  -   Velocity
//  -   EventDispatcher
//  -   DOM
//      -   element: Element purpose/role



// PROTOTYPES (4)
// --------------
// UserController
//     Short description

// UserLoginController
//     Short description

// UserClearSessionModel
//     Short description

// UserClearNewUserSession
//     Short description
//     - Functionality item 1
//     - Functionality item 2
//     - ...


var UserController = Backbone.View.extend({

    initialize: function(eventBus, userModel, userEmailVerificationModel){
        this.eventBus = eventBus;
        this.model = userModel;
        this.userEmailVerificationModel = userEmailVerificationModel;

        this.eventBus.on("validateEmailAddress", this.validateEmail, this);
    }
,   validateEmail: function(){
//        console.log("UserController.validateEmail(" + this.userEmailVerificationModel.attributes.email +")");
				delete this.userEmailVerificationModel.attributes.validateMore;
        var containingObject = this;
				try {
					this.userEmailVerificationModel.fetch({
						hideLoadingAnimation: true,
						type: "POST"
						, url: "/session/email"
						, contentType: "application/json"
						, data: JSON.stringify(containingObject.userEmailVerificationModel)
						, dataType: "json"
						, success: function() {
							containingObject.eventBus.trigger("validateEmailAddressResponse");
						}, error: function(data, textStatus, jqXHR) {
                            containingObject.eventBus.trigger("validateEmailAddressResponseFailure", textStatus);
                        }
					});
				} catch(e) {
//					console.log('there was an error');
				}
    }
});

var UserLoginController = Backbone.View.extend({


    initialize: function(eventBus, userModel){
        this.eventBus = eventBus;
        this.model = userModel;

    }
,   loginUser: function(userData, token){
		this.model.attributes.userData = userData;
		this.model.attributes.token = token;
		var containingObject = this;
		this.model.fetch({
			hideLoadingAnimation: true,
			type: 'POST'
			, async: false
			, url: '/session/internal'
			, contentType: 'application/json'
			, data: JSON.stringify(containingObject.model)
			, success: function(e){
//				console.log('loginUser.loginUser() success! ' + e);
                containingObject.eventBus.trigger('checkoutLoggedIn');
			}
			, error: function(jqXHR, textStatus, errorThrown){
//				console.log('UserController.loginUser() failed: ' + jqXHR);
                containingObject.eventBus.trigger('checkoutLoggedOut', textStatus);
                containingObject.eventBus.trigger('userREEErrorMsg', textStatus);
            }
		});
	},
	logoutUser:function ()
	{
		var containingObject = this;
		this.model.fetch({
			hideLoadingAnimation: true,
			type:'GET'
			, cache: false
			, url:'/session/logout'
			, contentType:'application/json'
			, data:JSON.stringify(containingObject.model)
			, success:function (e)
			{
//				console.log('UserController.logoutUser() success! ' + e)
				containingObject.eventBus.trigger('logoutUserSuccess');
			}, error:function (e)
			{
//				console.log('UserController.logoutUser() failed: ' + e)
			}
		});
	}
});




/*
var UserClearSessionModel = Backbone.Model.extend({

});



var UserClearNewUserSession = Backbone.View.extend({

    initialize: function(eventBus){
        this.eventBus = eventBus;
        this.userClearSessionModel = new UserClearSessionModel();

        this.eventBus.on("clearCart", this.clearSession, this);
    }
,   clearSession: function(){ 
        var pageId = $('body').attr("id");
        if(pageId !== 'download' && pageId !== 'checkout' && pageId !== 'terms'){
            var containingObject = this;
            this.userClearSessionModel.fetch({
				hideLoadingAnimation: true,
                type:'POST'
                , url:'/session/page'
                , contentType:'application/json'
                , success:function (e){ 
                }
                , error:function (e){ 
                }
            });
        }else{ 
        }
    }
});
*/