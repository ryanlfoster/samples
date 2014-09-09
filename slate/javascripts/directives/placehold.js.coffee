SLATE.directive 'placehold', ->

  {
    restrict: 'A'
    require: 'ngModel'
    link: (scope, element, attrs, ctrl) ->
      value = null

      replaceText = ->
        element.val(attrs.placehold)
      clearText = ->
        element.val('')

      scope.$watch attrs.ngModel, (val) ->
        value = val || ''

      element
        .bind 'focus', ->
          clearText() if value == ''
        .bind 'blur', ->
          replaceText() if element.val() == ''

      ctrl.$formatters.unshift (val) ->
        if !val
          replaceText()
          value = ''
          attrs.placehold
        else
          val
  }