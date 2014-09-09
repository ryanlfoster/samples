
window.SLATE.factory('Likeable', ['$http', '$resource', ($http, $resource) ->

  class Likeable

    constructor: (@url, @isLikedByMe, @numLikes) ->
      @resource = $resource @url

    hasManyLikes: ->
      @numLikes > 1

    hasLikes: ->
      @numLikes > 0

    isLikedByOthers: ->
      @hasManyLikes() || (@hasLikes() && !@isLikedByMe)

    getLikes: (success, error) =>
      @resource.query success, error

    like: ($event) =>
      $event.preventDefault()

      @isLikedByMe = true
      @numLikes++

      @resource.save((like, two, three) =>
        @likeId = like?.id
      , =>
        @isLikedByMe = false
        @numLikes--
      )

    unlike: ($event) =>
      $event.preventDefault()

      @isLikedByMe = false
      @numLikes--

      @resource.remove =>
        @likeId = -1
      , =>
        @isLikedByMe = true
        @numLikes++

  {
    create: (url, isLikedByMe, numLikes) ->
      return new Likeable url, isLikedByMe, numLikes

  }

])