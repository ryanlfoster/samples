window.SLATE.controller 'AlertCtrl', ['$scope', '$cookies', ($scope, $cookies) ->
  $scope.permaShowAlert = (alertId) ->
    cookieVal = $cookies['permaAlertToggle-' + alertId]
    !cookieVal || cookieVal != "false"

  $scope.permaDismissAlert = (alertId) ->
    $cookies['permaAlertToggle-' + alertId] = "false"
]