$(function() {
	mobile = ($(window).width() <= 480);

	// change links behaviour if on a touch device (most of these impose a 300ms delay when the user clicks something - we want to avoid that)
	var touch = "ontouchend" in document;

	if(typeof(windows) != 'undefined') { // defined in header.php <head>
		/* we want the font-face elements to be hidden until we are sure the font has loaded */
		$('.symbol').each(function() {
			$(this).css('visibility', 'visible');
		});
	}

	// NAVIGATION ARROWS BEHAVIOUR
	var arrow_nav = $('.arrow_nav');
	arrow_nav.show();
	arrow_nav.children('.previous, .next').click(function(e) {
		e.preventDefault();
		window.location.href = $(this).attr('href');
	});

	$(document).keydown(function(e) {
		var keycode = e.keyCode || e.which;

		if(e.altKey || e.shiftKey || e.ctrlKey || e.metaKey)
			return;

		if(keycode == 37) {
			arrow_nav.children('.previous').eq(0).click();
		} else if(keycode == 39) {
			arrow_nav.children('.next').eq(0).click();
		}
	});

	if(touch) {
		// fix navigation arrows
		$('a.skiphover').bind('hover', function(e) {
			window.location = $(this).attr('href');

			e.stopPropagation();
		});

	}

	ContactForm();
});

var ContactForm = (function() {                                                       
  /*                                                                                 
   * FORM BEHAVIOUR                                                                  
   * Build the form when the user clicks an open_letter link. The goal is to have the form hidden from bots and build it on demand.
   */                                                                                
                                                                                     
  var form_html = ['<form id="contact-form" method="post">',                         
        '<div id="message_wrapper">',                                                
          '<div class="right clearfix">',                                            
            '<div class="clearfix">',                                                
              '<label for="email">' + language.fromLabel + '</label>',                                    
              '<input type="text" name="email" id="email" class="required email" value="' + language.from + '" />',
            '</div>',                                                                
            '<div class="clearfix">',                                                
              '<label for="subject">' + language.subjectLabel + '</label>',                               
              '<input type="text" name="subject" id="subject" class="required" value="' + language.subject + '" />',
            '</div>',                                                                
          '</div>',                                                                  
          '<div class="clearfix"></div>',                                            
          '<textarea name="message" id="message" class="required">' + language.message + '</textarea>',                                                             
					'<div id="massive-error">',
						language.error,
						'<span>' + language.errorLegend + '</span>',
					'</div>',
        '</div>',                                                                    
        '<div id="footer_bottom" class="clearfix">',                                 
          '<span id="required_label">' + language.emailReminder + '</span>',
          '<input type="submit" id="submit-button" value="' + language.send + '" />',                 
          '<span id="cancel_button">' + language.cancel + '</span>', 
        '</div>',                                                                    
      '</form>'].join('\n'),                                                         
      created = false;

 // open the form when the open_letter link is clicked                               
  $('.open_letter').click(function(e) {                                              
    e.preventDefault();                                                              
                                                                                     
    if(!created) {                                                                   
      created = true;                                                                
      createForm();                                                                  
    }                                                                                
                                                                                     
    openLetter();                                                                    
  });                                                                                
                                                                                     
  // define variables in this scope for later use                                    
  var email, subject, message, letter, form;                                 
  var letterSize = {};                                                               
  var touch = "ontouchend" in document;                                              
                                                                                     
  function createForm() {                                                            
    // create form                                                                   
    $('#letter').html(form_html);                                                    
                                                                                     
    email = new Field($('#email'), null, $('#subject'));                             
    subject = new Field(email.next, email.field, $('#message'));                     
    message = new Field(subject.next, subject.field, null);                          
    letter = $('#contact_form');                                                     
    form = $('#contact-form');                                          
    success = $('#contact_success');                                                 

		// Localize the validator messages
		(function ($) {
			$.extend($.validator.messages, {
				required: language.errorRequired,
				email: language.errorEmail
			});
		}(jQuery));
                                                                                     
    // form validation                                                               
    form.validate({                                                                  
      onfocusout: false,                                                             
      submitHandler: function(form) {                                                
				var submitButton = $(form).find('#submit-button');
				submitButton.attr('disabled', 'disabled');
				submitButton.attr('value', language.sending);
				submitButton.addClass('is-disabled');

				var error = $(form).find('#massive-error');
				error.removeClass('is-visible');

        // when the form is submitted, make an ajax request to the php file          
        var data = {                                                                 
              from: email.field.val(),                                               
              subject: subject.field.val(),                                          
              message: message.field.val()                                           
            },                                                                       
            url = templateDir + '/php/contact.php',   
            callback = function(back) {
							submitButton.removeAttr('disabled');
							submitButton.removeClass('is-disabled');
							submitButton.attr('value', language.send);

							if(back == undefined || back == 1) {
								// hide the form & show the success message
								success.fadeIn(200);

								var height = $(form).innerHeight(),
										margin = $(form).css('margin'),
										padding = $(form).css('padding');

								$(letter).css('height', 'auto');

								$(form).animate({
									height: 0,
									margin: 0,
									padding: 0,
									opacity: 0
								}, 1000, function() {
									$(this).hide();
									$(this).css('height', height + 'px');
									$(this).css('margin', margin);
									$(this).css('padding', padding);
									$(this).css('opacity', 1);
								});
							} else {
								error.addClass('is-visible');
							}
						};
				$.post(url, data, callback, 'json');
			}
		});

		// fields auto grow magic
		if(!mobile) {
			email.field.autoGrowInput({
				comfortZone: 20
			});

			subject.field.autoGrowInput({
				comfortZone: 20
			});
		}

		// close the form when the cancel button is clicked
		$("#cancel_button").click(function(e) {
			e.preventDefault();
			closeLetter();
			document.activeElement.blur();
		});
	}

	/*
	 * Helper function that reveals the contact form
	 */
	function openLetter() {
		success.fadeOut(200);
		form.show();
		letter.show();

		// message grow magic
		message.field.autogrow();

		if(!touch) {
			$("html, body").animate({
				scrollTop: letter.offset().top + letter.outerHeight() - $(window).innerHeight() + 16
			}, 200);
		}

		// select the first field so that the user understands this is editable
		email.field.focus().selectRange(email.field.val().length, email.field.val().length);
	}

	/*
	 * Helper function that hides the contact form
	 */
	function closeLetter() {
		letterSize.height = letter.innerHeight();
		letterSize.marginTop = letter.css('margin-top');
		letterSize.padding = letter.css('padding');

		letter.animate({
			height: 0,
			marginTop: 0,
			padding: 0,
			opacity: 0
		}, 1000, function() {
			$(this).hide();
			$(this).css({
				height: letterSize.height + "px",
				marginTop: letterSize.marginTop,
				padding: letterSize.padding,
				opacity: "1"
			});
		});

		// trigger zoom out in iOS (Safari automatically zooms in when the contact box is focused)
		var vp = $('meta[name="viewport"]').attr('content');
		$('meta[name="viewport"]').attr('content', vp + " maximum-scale=1.0");
		$('meta[name="viewport"]').attr('content', vp);

		$("html, body").animate({
			scrollTop: $('#contact').offset().top 
		}, 200);
	}

	/*
	 * FIELD OBJECT
	 */

	function Field(field, prev, next) {
		this.field = field;

		if(prev != null) {
			this.prev = prev;
			this.prevField();
		}

		if(next != null) {
			this.next = next;
			this.nextField();
		}
	}

	Field.prototype.prevField = function() {
		var thisRef = this;

		this.field.keydown(function(e) {
			var keycode = e.keyCode || e.which;

			if((keycode == 38 || keycode == 37) && $(this).getCursorPosition() == 0) { // up or left
				e.preventDefault();

				thisRef.prev.focus();
				thisRef.prev.selectRange(thisRef.prev.val().length, thisRef.prev.val().length);
			}

			e.stopPropagation();
		});	
	};

	Field.prototype.nextField = function() {
		var thisRef = this;

		this.field.keydown(function(e) {
			var keycode = e.keyCode || e.which;

			if(keycode == 40 || (keycode == 39 && $(this).getCursorPosition() == $(this).val().length)) { // down & right
				e.preventDefault();
				thisRef.next.focus();
			}

			e.stopPropagation();
		});
	};



	(function($) {

		/*
		 * Auto-growing textareas; technique ripped from Facebook
		 */
		$.fn.autogrow = function(options) {

			this.filter('textarea').each(function() {
					
				var $this       = $(this),
						minHeight   = $this.height(),
						lineHeight  = $this.css('lineHeight'),
						touch = "ontouchend" in document;
				
				var shadow = $('<div></div>').css({
						position:   'absolute',
						top:        -10000,
						left:       -10000,
						width:      $this.outerWidth() - parseInt($this.css('paddingLeft')) - parseInt($this.css('paddingRight')),
						fontSize:   $this.css('fontSize'),
						fontFamily: $this.css('fontFamily'),
						lineHeight: $this.css('lineHeight'),
						resize:     'none'
				}).appendTo(document.body);

				var update = function(event) {
					var times = function(string, number) {
						for (var i = 0, r = ''; i < number; i ++) r += string;
						return r;
					};
					
					var val = this.value.replace(/</g, '&lt;')
															.replace(/>/g, '&gt;')
															.replace(/&/g, '&amp;')
															.replace(/\n$/, '<br/>&nbsp;')
															.replace(/\n/g, '<br/>')
															.replace(/ {2,}/g, function(space) { return times('&nbsp;', space.length -1) + ' ' });

					shadow.html(val);
					$(this).css('height', Math.max(shadow.height() + parseInt(lineHeight), minHeight));

					// fix shadow bug
					var wrapper = $('#message_wrapper');
					resetCss(wrapper, 'boxShadow');
					resetCss(wrapper, 'WebKitBoxShadow');
					resetCss(wrapper, 'MozBoxShadow');
				}

				$(this).keyup(update).keydown(update);

				// trigger the keydown event to make the box adjust its height before the user sees it.
				$(this).trigger('keydown');
			});

			function resetCss(elem, property) {
				var original = elem.css(property);

				if(original != null) {
					elem.css(property, 'none');
					elem.css(property, original);
				}
			}

			return this;
		}

		/* from: http://stackoverflow.com/a/931695 */
		$.fn.autoGrowInput = function(o) {

				o = $.extend({
						maxWidth: 1000,
						minWidth: 0,
						comfortZone: 70
				}, o);

				this.filter('input:text').each(function(){

						var minWidth = parseInt($(this).css('min-width').replace('px','')),
								maxWidth = parseInt($(this).css('max-width').replace('px','')),
								val = '',
								input = $(this),
								testSubject = $('<tester/>').css({
										position: 'absolute',
										top: -9999,
										left: -9999,
										width: 'auto',
										fontSize: input.css('fontSize'),
										fontFamily: input.css('fontFamily'),
										fontWeight: input.css('fontWeight'),
										letterSpacing: input.css('letterSpacing'),
										whiteSpace: 'nowrap'
								}),
								check = function() {

										if (val === (val = input.val())) {return;}

										// Enter new content into testSubject
										var escaped = val.replace(/&/g, '&amp;').replace(/\s/g,'&nbsp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
										testSubject.html(escaped);

										// Calculate new width + whether to change
										var testerWidth = testSubject.width(),
												newWidth = (testerWidth + o.comfortZone) >= minWidth ? testerWidth + o.comfortZone : minWidth,
												currentWidth = input.width(),
												isValidWidthChange = (newWidth < currentWidth && newWidth >= minWidth)
																						 || (newWidth > minWidth && newWidth < maxWidth);

										// Animate width
										if (isValidWidthChange) {
												input.width(newWidth);
										}

								};

						testSubject.insertAfter(input);

						$(this).bind('keyup keydown blur update', check);
						check();
				});

				return this;
		};

		// from http://stackoverflow.com/a/1909997
		$.fn.getCursorPosition = function() {
			var pos = 0;
			var el = $(this).get(0);

			// IE Support
			if (document.selection) {
				el.focus();
				var Sel = document.selection.createRange();
				var SelLength = document.selection.createRange().text.length;
				Sel.moveStart('character', -el.value.length);

				// hack
				if(Sel.text.length-SelLength < el.selectionStart)
					pos = Sel.text.length - SelLength;
				else
					pos = el.selectionStart;
			}
			// Firefox support
			else if (el.selectionStart || el.selectionStart == '0')
				pos = el.selectionStart;

			return pos;
		}

		// from http://stackoverflow.com/a/841121
		$.fn.selectRange = function(start, end) {
			return this.each(function() {
					if (this.setSelectionRange) {
							this.focus();
							this.setSelectionRange(start, end);
					} else if (this.createTextRange) {
							var range = this.createTextRange();
							range.collapse(true);
							range.moveEnd('character', end);
							range.moveStart('character', start);
							range.select();
					}
			});
		};
	})(jQuery);
});
// end of ContactForm 



/* PSEUDO EVENTS FOR INTERNET EXPLORER */
// by Luke Lutman - http://jquery.lukelutman.com/plugins/pseudo/
(function($){

	var patterns = {
		text: /^['"]?(.+?)["']?$/,
		url: /^url\(["']?(.+?)['"]?\)$/
	};

	function clean(content) {
		if(content && content.length) {
			var text = content.match(patterns.text)[1],
				url = text.match(patterns.url);
			return url ? '<img src="' + url[1] + '" />': text;
		}
	}

	function inject(prop, elem, content) {
		if(prop != 'after') prop = 'before';
		if(content = clean(elem.currentStyle[prop])) {
			$(elem)[prop == 'before' ? 'prepend' : 'append'](
				$(document.createElement('span')).addClass(prop).html(content)
			);
		}
	}

	$.pseudo = function(elem) {
		inject('before', elem);
		inject('after', elem);
		elem.runtimeStyle.behavior = null;
	};
	
	if(document.createStyleSheet) {
		var o = document.createStyleSheet(null, 0);
		o.addRule('.dummy','display: static;');
		o.cssText = 'html, head, head *, body, *.before, *.after, *.before *, *.after * { behavior: none; } * { behavior: expression($.pseudo(this)); }';
	}

})(jQuery);


/*! A fix for the iOS orientationchange zoom bug.
 Script by @scottjehl, rebound by @wilto.
 MIT License.
*/
(function(w){

	// This fix addresses an iOS bug, so return early if the UA claims it's something else.
	if( !( /iPhone|iPad|iPod/.test( navigator.platform ) && navigator.userAgent.indexOf( "AppleWebKit" ) > -1 ) ){
		return;
	}

    var doc = w.document;

    if( !doc.querySelector ){ return; }

    var meta = doc.querySelector( "meta[name=viewport]" ),
        initialContent = meta && meta.getAttribute( "content" ),
        disabledZoom = initialContent + ",maximum-scale=1",
        enabledZoom = initialContent + ",maximum-scale=10",
        enabled = true,
		x, y, z, aig;

    if( !meta ){ return; }

    function restoreZoom(){
        meta.setAttribute( "content", enabledZoom );
        enabled = true;
    }

    function disableZoom(){
        meta.setAttribute( "content", disabledZoom );
        enabled = false;
    }

    function checkTilt( e ){
		aig = e.accelerationIncludingGravity;
		x = Math.abs( aig.x );
		y = Math.abs( aig.y );
		z = Math.abs( aig.z );

		// If portrait orientation and in one of the danger zones
        if( !w.orientation && ( x > 7 || ( ( z > 6 && y < 8 || z < 8 && y > 6 ) && x > 5 ) ) ){
			if( enabled ){
				disableZoom();
			}        	
        }
		else if( !enabled ){
			restoreZoom();
        }
    }

	w.addEventListener( "orientationchange", restoreZoom, false );
	w.addEventListener( "devicemotion", checkTilt, false );

})( this );
