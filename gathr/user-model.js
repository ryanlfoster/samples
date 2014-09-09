var UserAccountSecurityQuestionModel = Backbone.Model.extend({
    initialize:function(formValidators) {
        this.formValidators = formValidators == undefined ? new FormValidators() : formValidators;
    }
,   validation:{
        securityQuestion: {
            fn: 'validSecurityQuestion'
        }
        ,   securityQuestionAnswer: {
        	fn: 'validSecurityQuestionAnswer'
        }
    }
,   validSecurityQuestion: function(value, attr, computedState){
        var value = $('#securityQuestionASQ').val();
		if (typeof value === 'undefined' || value === '') {
			return 'Please select a security question';
		}
		else {
			return;
		}
    }
,   validSecurityQuestionAnswer: function(xvalue, attr, computedState) {
		var value = $('#SolosecurityAnswer').val(), regx = new RegExp(/^[a-zA-Z0-9 ]+$/),
		msg = 'To help in case you forget your password, please select a security question and answer using 3-16 letters and numbers.';
    	if (typeof value === 'undefined' || value.length < 3 || value.length > 16) {
    		return msg;
    	}
        else if (!regx.test(value)) {
            return msg;
        }
    	else {
    		return;
    	}
	}
});

var UserLoggedInModel = Backbone.Model.extend({

});

var UserCurrentProductsModel = Backbone.Model.extend({

});

var UserEmailVerificationModel = Backbone.Model.extend({
	initialize:function(formValidators) {
		this.formValidators = formValidators == undefined ? new FormValidators() : formValidators;
	}
	, defaults: {
		validateMore: false
	}
	, urlRoot:"/session/email"
	, validation:{
		email:{
			required:true
			, fn: 'validEmail'
			, msg:'Please enter a Valid Email Address'
		}
	}
	, validEmail: function(value, attr, computedState){
		var value = this.formValidators.validEmail(value, attr, computedState);
		this.validateMore = false;
		return value;
	}
});

var UserModel = Backbone.Model.extend({
	/*
	 defaults: {
	 email: ""
	 ,   emailConfirmation: ""
	 ,   password: ""
	 ,   passwordConfirmation: ""
	 ,   firstName: ""
	 ,   lastName: ""
	 ,   dateOfBirth: ""
	 ,   securityQuestion: ""
	 ,   securityAnswer: ""
	 ,   userExists: false
	 }
	 ,   */
	initialize:function(formValidators) {
			this.formValidators = formValidators == undefined ? new FormValidators() : formValidators;
            this.validateDOBData = false;

	}
	, urlRoot:"/session/user"
	, validation:{
		email:{
			required:true
			, fn:'validEmail'
		}
		, emailConfirmation:{
			required:true
			, fn: 'validEmailConfirmation'
		}
		, password:{
			required:true
			, minLength:6
			, maxLength:16
			, fn: 'validPassword'
		}
		, passwordConfirmation:{
			required:true
			, fn : 'validatePasswordConfirmation'
		}
		, firstName:{
			required:true
                        , fn: 'validateFirstName'
                        , msg: ''
		}
		, lastName:{
			required:true
                        , fn: 'validateLastName'
                        , msg: ''
		}
		, dateOfBirth:{
            required:true
            , pattern:'^\\d{4}$'
            , msg:'Please enter your date of birth'
            , fn: 'validateDOB'
		}
		, securityQuestion:{
			minLength: 1
          , msg: 'Please select a question.'
			, required: true
		}
		, securityAnswer:{
			required:true
			, fn: 'validSecurityAnswer'
		}
	}
	, validEmailConfirmation:function(value, attr, computedState){
		return this.formValidators.validEmailConfirmation(value, attr, computedState);
	}
	, validSecurityAnswer: function(xvalue, attr, computedState) {
		var value = $('#securityAnswer').val(), regx = new RegExp(/^[a-zA-Z0-9 ]+$/),
		msg = 'To help in case you forget your password, please select a security question and answer using 3-16 letters and numbers.';
    	if (typeof value === 'undefined' || value.length < 3 || value.length > 16) {
    		return msg;
    	}
        else if (!regx.test(value)) {
            return msg;
        }
    	else {
    		return;
    	}
	}
	, validPassword:function(value, attr, computedState) {
		// no spaces allowed
		var regex = /^\S*$/;
		if (! regex.test(value)) return "Password must not contain spaces.";
		return this.formValidators.validPassword(value, attr, computedState);
	}
	, validatePasswordConfirmation: function(value, attr, computedState) {
        var pswd = $('#password').val(), cpswd = $('#passwordConfirmation').val();
        if (pswd !== cpswd) {
            return 'The passwords you entered do not match. Please re-enter your password exactly the same in both fields.';
        } 
    }
        ,   validateFirstName: function(xvalue, attr, computedState) {
            var value = $('#createAccountFirstName').val(), regx = new RegExp(/^[a-zA-Z ]+$/);
            if (value === undefined || value.length === 0 || value === '') {
                return 'Please enter your First Name.';
            }
            else if (value !== '' && value.length < 2) {
                return 'Please enter at least two characters for First Name.';
            }
            else if (value !== '' && value.length > 15) {
                return 'This name may be too long. Please try again.';
            }
            else if (!regx.test(value)) {
                return 'First Name cannot contain numbers or characters such as @, !, * or $.';
            }
            else {
                return;
            }

        }
        ,   validateLastName: function(xvalue, attr, computedState) {
            var value = $('#createAccountLastName').val(), regx = new RegExp(/^[a-zA-Z ]+$/);
            if (value === undefined || value.length === 0 || value === '') {
                return 'Please enter you Last Name.';
            }
            else if (value !== '' && value.length < 2) {
                return 'Please enter at least two characters for Last Name.';
            }
            else if (value !== '' && value.length > 15) {
                return 'This name may be too long. Please try again.';
            }
            else if (!regx.test(value)) {
                return 'Last Name cannot contain numbers or characters such as @, !, * or $.';
            }
            else {
                return;
            }
        }
        , validEmail:function(value, attr, computedState) {
		var tmpState = _.extend({}, computedState, {validateMore:true});
		var valid = this.formValidators.validEmail(value, attr, tmpState);
		return valid;
	}
    , validateDOB: function(value, attr, computedState){
        if(!this.validateDOBData === true){
            return false;
        }
    }

});


var LendTokenModel = Backbone.Model.extend({

});
