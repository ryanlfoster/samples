window.SLATE.controller 'WallCtrl', ['$scope', '$http', '$element', '$timeout', 'Likeable', 'numberUtil', 'stringUtil', 'Wall', ($scope, $http, $element, $timeout, Likeable, numberUtil, stringUtil, Wall) ->
  wall = Wall
  wall.url = ''
  wall.reset window.bootstrapThanksData

  $scope.thanks = wall.thanks

  $scope.startingPage = 2
  spinner = $ '.wall-load-more', $element
  phrases = spinner.find('.load-more-phrase:not(.error)')

  spinner.addClass 'is-hidden'

  # $scope.isLoading = false
  $scope.likeables = []

  isEndOfThanks = (data) ->
    data?.thanks?.length == 0

  forceScrollBar = ->
    $('body').css 'min-height', $(window).height() + 20

  $scope.showLikesModal = ($event, indx) ->
    $event.preventDefault()

    $scope.$emit 'analytics:track', 'view likes modal', { state: 'show' }

    $scope.likeables[indx].getLikes (data) ->
      $scope.modalLikes = data
      $('#likes-modal').modal()
    , (->)

  $scope.create = Likeable.create
  $scope.slugify = stringUtil.slugify

  $scope.findAccount = (accounts, id) ->
    _(accounts).find (account) ->
      account.id == id

  $scope.loadNextPage = =>
    $scope.isLoading = true

    # TODO: make more angular'ish -- use ng-show and $scope.myFlags
    phrases.hide()
    phrases.eq(numberUtil.getRandomInt(0, phrases.length-1)).show()
    spinner.removeClass 'is-hidden'

    $timeout ->
      wall.fetch()
        .success (data) ->
          spinner.addClass 'is-hidden'

          # TODO
          $scope.$emit('analytics:track', 'view more wall items')

          # TODO: use error from json to detect end of list
          if isEndOfThanks(data)
            $scope.isLoading = true
          else
            $scope.isLoading = false

            # HACK: always force a scroll bar
            forceScrollBar()

        .error (data) ->
          $scope.isLoading = true
          phrases.hide()
          spinner.find('.load-more-icon').hide()
          spinner.find('.load-more-phrase.error').show()
    , 1000

  $scope.toggleCommentsLimit = (thank) ->
    if thank?.commentsLimit == -2
      thank.commentsLimit = -50
    else
      thank?.commentsLimit = -2

  $(window).bind 'scroll', ->
    win = $(window)
    unless $scope.isLoading
      if win.scrollTop() + win.height() >= ($element.offset().top + $element.height() - 150)
        $scope.loadNextPage()

  forceScrollBar()

]
