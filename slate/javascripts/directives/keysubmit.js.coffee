SLATE.directive 'keysubmit', ->
  {
    restrict: 'A'
    link: (scope, element, attributes)->
      $(element).on 'keydown', (evt) ->
        if (evt.ctrlKey || evt.metaKey) && evt.which == 13
          scope.$apply attributes.keysubmit
  }
