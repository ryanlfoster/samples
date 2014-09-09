window.SLATE.controller('ThankCtrl', ['$scope', '$http', '$location', 'Likeable', ($scope, $http, $location, Likeable) ->

  $scope.create = Likeable.create

  $scope.showLikesModal = ($event) ->
    $event.preventDefault()
    $scope.likeable.getLikes((data) ->
      $scope.modalLikes = data
      $('#likes-modal').modal()
    , (->))

])
