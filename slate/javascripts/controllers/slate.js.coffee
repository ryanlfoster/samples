window.SLATE = angular.module('slate', ['ngCookies', 'ngResource', 'analytics'])
  .config ['$httpProvider', ($httpProvider) ->
    $httpProvider.defaults.headers.common['X-CSRF-Token'] = $('meta[name=csrf-token]').attr('content')
  ]

window.SLATE.controller 'SlateCtrl', ['$scope', 'analytics', ($scope, analytics) ->

  $scope.loginToggle = false
  $scope.toggleLogin = ($event) ->
    $scope.$emit 'analytics:track', 'login dialog', { state: if $scope.loginToggle then 'hide' else 'show' }
    $scope.loginToggle = !$scope.loginToggle
    $scope.forgotPasswordToggle = false
    $event.preventDefault()

  $scope.forgotPasswordToggle = false
  $scope.toggleForgotPassword = ($event) ->
    $scope.forgotPasswordToggle = !$scope.forgotPasswordToggle
    $event.preventDefault()

  unless Modernizr.placeholder
    $('input, textarea').not('[placehold]').placeholder()

]
