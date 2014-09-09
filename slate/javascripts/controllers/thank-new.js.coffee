SLATE.controller 'ThanksNewCtrl', ['$scope', '$element', '$http', ($scope, $element, $http)->

  $scope.init = (badges, currentBadgeId) ->

    $('.email', $element).typeahead
      source: _.debounce(fetchAccounts, 200)

      updater: (item)->
        cur = @.$element.val().replace(/[^,;]*$/,'')

        # append to current value and don't forget the trailing comma
        "#{ cur } #{ item.replace(/.*\<(.+)\>.*/, "$1") }, "

      matcher: (item)->
        query = multiEmailExtractor @query

        ~item.toLowerCase().indexOf query.toLowerCase()

      highlighter: (item)->
        query = this.query.replace /[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&'
        item.replace new RegExp('(' + query + ')ig'), ($1, match)->
          "<strong>#{ match }</strong>"

    $scope.badges = badges
    $scope.currentBadgeId = currentBadgeId
    slugifyAll()
    $scope.chooseBadge()

  multiEmailExtractor = (query)->
    # match anything that doesn't end in a comma
    result = /([^,;]+)$/.exec query

    if result? and result[1]
      $.trim(result[1])
    else
      ''

  fetchAccounts = (query, callback)->
    q = multiEmailExtractor query

    $.get '/accounts/suggestions', { q: q }, (data)->
      sourceData = _.map data, (account)->
        "#{account.display_name} <#{account.email}>"
      callback sourceData

  slugifyAll = ->
    _($scope.badges).each (badge) ->
      badge.slug = slugify badge.name

  getRandomInt = (min, max) ->
    Math.floor(Math.random() * (max - min + 1)) + min

  findRandomBadge = ->
    $scope.badges[getRandomInt(0, $scope.badges.length - 1)]

  findBadgeById = (badgeId) ->
   return _.find $scope.badges, (badge) ->
    badge.id is badgeId

  slugify = (slug) ->
    slug = slug.toLowerCase()
    slug = slug.split(' ').join('-')
    slug = slug.replace(/[^0-9A-Za-z\-]/, '')
    slug

  $scope.chooseBadge = ($event, badge) ->
    if $event?
      $event.preventDefault()
      $($event.target).closest('.modal').modal('hide')
      $scope.badge = badge
    else
      if $scope.currentBadgeId > 0
        $scope.badge = findBadgeById $scope.currentBadgeId
      else
        $scope.badge = findRandomBadge()
]
