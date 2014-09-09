SLATE.controller 'AcctAvatarCtrl', ['$scope', '$element', '$http', ($scope, $element, $http) ->
  fileInput = $element.find('.profile-form input[type=file]')
  uploader = null

  clearList = ->
    _.delay ->
      $element.find('.qq-upload-list').fadeOut ->
        $(this).html('').show()
    , 2000

  $scope.startFileUpload = ->
    unless $scope.isIE
      $(uploader).find('.qq-upload-button input[type=file]').click()


  unless $scope.isIE
    uploader = $element.find('.uploader').fineUploader
      request:
        endpoint: "/accounts/avatar"
        customHeaders:
          "X-CSRF-Token": $('meta[name=csrf-token]').attr('content')

    uploader.on 'complete', (evt, u, file, avatarUrl)->
      $scope.$apply ->
        $scope.avatar_url = avatarUrl.src
        clearList()

    $element.find('.qq-upload-drop-area, .qq-upload-button, .qq-drop-processing').hide()

]
