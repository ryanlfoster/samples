function FormValidators() {
	this.validEmail = function(value, attr, computedState) {
		var msg;
		var pattern = /^(?:[\w\#\-]+\.)*[\w\#\-\.]+@(?:(?:(?:[a-zA-Z0-9_](?:[a-zA-Z0-9_\-](?!\.)){0,61}[a-zA-Z0-9_]?\.)+[a-zA-Z0-9_](?:[a-zA-Z0-9_\-](?!$)){0,61}[a-zA-Z0-9_]?)|(?:\[(?:(?:[01]?\d{1,2}|2[0-4]\d|25[0-5])\.){3}(?:[01]?\d{1,2}|2[0-4]\d|25[0-5])\]))$/;
		var pattern2 = /^(?:[\w\#\-]+\.)*[\w\#\-\.]+@((aol\.com)|(aim\.com)|(aol\.co\.uk)|(aol\.fr)|(aol\.de)|(compuserve\.com)|(netscape\.net)|(about\.me))$/;
		var pattern3 = /^(?:[\w\#\-]+\.)*[\w\#\-\.]+@((mac\.com)|(me\.com)|(icloud\.com))$/;

		try {
			if(value === undefined || value.length == 0) {
				msg = 'Please enter a valid email address.';
			}
			else {
				if(!value.match(pattern)) {
					msg = 'Please enter a valid email address';
				} else if(computedState.validateMore) {
					if(value.match(pattern2)) {
						msg = 'Please enter an existing personal email address';
					}
					else if(value.match(pattern3)) {
						msg = 'The email domain cannot be used for Gathr. Please use another email address.';
					}
				}
			}
		} catch(e) {
			msg = 'Unable to validate email!';
		}
		return msg;
	};

	this.validEmailConfirmation = function(value, attr, computedState){
		try {
			if(!compareStringsNoCase(value, computedState.email)) {
				return 'The Email Addresses you entered do not match. Please reenter your Email Address exactly the same in both fields.';
			}
		} catch(e) {
		}
	};

	this.validSecurityQuestion = function(value, attr, computedState) {
		if(value === '0' || value === undefined) {
			return 'Please select a security question';
		}
	};

	this.validPassword = function(value, attr, computedState) {
		var msg = "Password should contain 6 to 16 characters and a combination of upper and lower case letters, numbers and special characters (e.g., @, #, &). Do not use common or easily guessed words in your password (e.g. first, last, or screen name)",
        	msg2 = 'Your password is too similar to your email address or first and last name. Please enter a new password.';
		var compareLen = 4;
		try {
            var isPasswordBlocked2 = isPasswordBlocked(value);
            if (isPasswordBlocked2) {
                return msg;
            }
            // there are several stages to the validation this function does.
            var doesPasswordMatchEmail = value === computedState.email;
            var hasSingleCharacterDiff2 = hasSingleCharacterDiff(computedState.email, value);
            var compareSNandPassword2 = compareSNandPassword(computedState.email, value, compareLen);
            if (doesPasswordMatchEmail || !hasSingleCharacterDiff2 || !compareSNandPassword2) {
                return msg;
            }

            if(!comparePassName($('[name=firstName]').val(), $('[name=lastName]').val(), value)) {
				return msg2;
			}

		} catch(e) {
//            console.log(e);
		}
	};

	this.validCreditCard = function(value, attr, cardType) {
		var msg;
		var number;
		var valid = true;
		//var cardType = computedState.cardType;
		if(cardType === undefined) {
			msg = 'Please specify a card type.';
		} else if (value === undefined || value.length == 0){
			msg = 'Please enter a valid credit card number.';
		} else {
			number = value.replace(/[^0-9]/g, '')
			cardType = cardType.toLowerCase().replace(/[_\s]/g,'');
			switch(cardType) {
				case 'visa':
				case 'visadebit':
					if(!charAtIsArg(number, '4', 0)) {
						return "Please make sure you have entered your Visa card number correctly.";
					}
					else if(number.length != 13 && number.length != 16){
						return 'Please make sure you have entered the correct number of digits, no spaces.'
					}
					break;
				case 'mastercard':
				case 'mastercarddebit':
					if(!charAtIsArg(number, '5', 0) && (!compareArgToCharAt(number, '1', 1, '<') || !compareArgToCharAt(number, '4', 1, '>'))) {
						return "Please make sure you have entered your MasterCard card number correctly.";
					}
					else if(number.length != 16) {
						return 'Please make sure you have entered the correct number of digits, no spaces.';
					}
					break;
				case 'americanexpress':
					if(!charAtIsArg(number, '3', 0) || !charAtIsArg(number, '4', 1) && !charAtIsArg(number, '7', 1)) {
						return "Please make sure you have entered your American Express credit card number correctly.";
					}
					else if(number.length != 15) {
						return 'Please make sure you have entered the correct number of digits, no spaces.';
					}
					break;
				case 'discover':
					if(!subStringMatch(number, 0, 4, '6011')) {
						return "Please make sure you have entered your Discover credit card number.";
					}
					else if(number.length != 16) {
						return 'Please make sure you have entered the correct number of digits, no spaces.';
					}
					break;
			}
			
			cs = ccCheckSum(number);
			if (cs!=0) {
				return 'The card numbers do not appear to be valid, please try again.';
			}
		}

		return msg;
	};

	function compareStringsNoCase(str1, str2)
	{
		str1 = str1.toLowerCase();
		str2 = str2.toLowerCase();
		if (str1 == str2)
			return true;
		return false;
	}

	function subStringMatch(str, start, length, substr) {
		if(str.substring(start, length) == substr) return 1;
		return 0;
	}

	function ccCheckSum(number) {
		total = 0;
		tmp = 0;
		for(var loc = number.length - 2; loc >= 0; loc -= 2) {
			total += 1 * number.charAt(loc + 1);
			tmp = number.charAt(loc) * 2;
			if(tmp > 9) total += 1;
			total += tmp % 10;
		}
		if(number.length % 2 > 0) total += 1 * number.charAt(0);
		return total % 10;
	}

	function comparePassName(firstName, lastName, password) {
		if((firstName.length >= 4 && password.match(firstName)) || (lastName.length >= 4 && password.match(lastName))) {
			return 0;
		}
		return 1;
	}

	function hasSingleCharacterDiff(string1, string2) {
		string1 = string1.toLowerCase();
		string2 = string2.toLowerCase();
		for(var _i = 0; _i < string2.length; _i++) {
			if(string1.indexOf(string2.charAt(_i)) == -1) return true;
		}
		return false;
	}

	function compareSNandPassword(screenname, password, subLen) {
		screenname = screenname.toLowerCase();
		password = password.toLowerCase();
		snLen = screenname.length;
		pwLen = password.length;
		snLessLen = snLen - subLen;
		pwLessLen = pwLen - subLen;
		if (pwLessLen <= 3){
			pwLessLen = 4;
		}
		if (screenname.charAt(3) === '@') {
			subLen = 3;
		}
		for(var i = 0; i < snLessLen; i++) {
			iPlusLen = i + subLen;
			snSnip = screenname.substring(i, iPlusLen);

			for(var j = 0; j < pwLessLen; j++) {
				jPlusLen = j + subLen;
				pwSnip = password.substring(j, jPlusLen);
				if(pwSnip == snSnip) {
					return false;
				}
			}
		}
		return true;
	}

    function isPasswordBlocked(password) {
        var blockedPasswords = new Array(
            "1111", "1234", "7777", "11111", "12345", "55555", "111111", "112233", "121212", "123123", "123321", "123456", "222222", "654321", "666666",
            "696969", "1234567", "7777777", "11111111", "12345678", "123456789", "123456a", "123abc", "123qwe", "1q2w3e", "1q2w3e4r", "aaaaaa", "abc123",
            "adidas", "admin", "alexis", "amanda", "andrew", "angel", "angel1", "angels", "anthony", "aol123", "aol2005", "apple", "asdf", "asdfasdf", "asdfgh",
            "ashley", "ashley1", "asshole", "asshole1", "austin", "baby", "babygirl", "bailey", "banana", "bandit", "baseball", "baseball1", "batman", "benjamin",
            "bitch1", "biteme", "blabla", "blahblah", "blessed", "blessing", "blink182", "brandon", "brittany", "bubbles", "buster", "canada", "cassie", "charlie", "cheer1",
            "cheese", "chelsea", "chicken", "chris", "chris1", "christ", "church", "cocacola", "compaq", "compusa", "computer", "cookie", "cool", "corvette", "creative",
            "dakota", "dallas", "dancer1", "daniel", "danielle", "david", "destiny", "dexter", "diamond", "digital", "dragon", "eagles1", "eminem", "emmanuel", "enter",
            "faith", "family", "flower", "flower1", "foobar", "football", "football1", "forever", "forum", "freedom", "friend", "friends", "fucker1", "fuckoff", "fuckyou",
            "fuckyou!", "fuckyou1", "fuckyou2", "gateway", "genesis", "george", "ginger", "god", "google", "grace", "green", "guitar", "hahaha", "hallo", "hannah",
            "happy", "hardcore", "harley", "heaven", "hello", "hello1", "helpme", "hockey", "hope", "hotdog", "hottie", "hunter", "hunter", "ilovegod", "iloveyou",
            "iloveyou!", "iloveyou1", "iloveyou2", "internet", "james", "jasmine", "jason", "jasper", "jennifer", "jessica", "jesus", "jesus1", "john", "john316", "johnny",
            "jordan", "jordan23", "joseph", "joshua", "junior", "justin", "killer", "kitten", "knight", "letmein", "london", "looking", "love", "love123", "lovely", "loveme",
            "lovers", "loving", "lucky", "maggie", "master", "matrix", "matthew", "maverick", "maxwell", "melissa", "merlin", "mexico", "michael", "michelle", "mickey",
            "microsoft", "mike", "monkey", "monkey1", "mother", "muffin", "mustang", "mylove", "myspace1", "nathan", "nicole", "nicole1", "nigga1", "nigger1",
            "nintendo", "none", "nothing", "onelove", "online", "orange", "orange1", "pass", "passw0rd", "password", "password1", "password2", "pastor", "patrick",
            "peace", "peaches", "peanut", "pepper", "phpbb", "pokemon", "poop", "power", "praise", "prayer", "prince", "princess", "princess1", "purple", "qazwsx",
            "qwert", "qwerty", "qwerty1", "rachel", "rainbow", "red123", "richard", "robert", "rotimi", "samantha", "sammy", "samuel", "saved", "school", "scooby",
            "scooter", "secret", "shadow", "shalom", "shorty", "silver", "single", "slayer", "smokey", "snoopy", "soccer", "soccer1", "soccer2", "soleil13", "sommer",
            "sparky", "spirit", "startrek", "starwars", "stella", "steven", "summer", "summer06", "summer1", "sunshine", "superman", "taylor", "test", "testing", "testtest",
            "thomas", "thunder", "tigger", "trinity", "trustno1", "tweety", "victoria", "victory", "viper", "volcom1", "welcome", "whatever", "william", "winner", "wisdom",
            "yellow", "zxcvbnm"
        );
        for(var i=0; i < blockedPasswords.length; i++) {
            if (blockedPasswords[i] === password) {
                return true;
            }
        }
        return false;
    }

	function charAtIsArg(str, chr, index) {
		if(str.charAt(index) == chr) return true;
		return false;
	}

	function compareArgToCharAt(str, chr, index, compOp) {
		if(eval('str.charAt(index)' + compOp + 'chr')) return 1;
		return 0;
	}
}
