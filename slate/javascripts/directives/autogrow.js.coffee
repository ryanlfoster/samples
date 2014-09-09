SLATE.directive 'autogrow', ->
  {
    restrict: 'A'
    link: (scope, element)->
      isFirefox = !!navigator.userAgent.match(/firefox/i)
      el = $(element).css { overflow: 'hidden' }

      inputResize = ->
        vertPadding = parseInt(el.css('padding-top'), 10) + parseInt(el.css('padding-bottom'), 10)

        # tiny delay for a smoother height change
        _.delay ->
          if isFirefox then el.height el[0].scrollHeight
          else el.height el[0].scrollHeight - vertPadding
        , 1

      inputRThrottle = _.throttle inputResize, 400

      el.on 'change keydown paste', ->
        inputResize()

      $(window).on 'resize', ->
        inputRThrottle()
  }
