SLATE.factory 'Thank', ['$http', 'stringUtil', ($http, stringUtil)->
  class Thank
    comments: []
    errors: []
    hasDirtyComment: false
    newComment: ''

    constructor: (options) ->
      _.extend @, options

    resetErrors: (errs=[]) =>
      @errors = errs

    formatComment: (comment) ->
      if _.isString comment
        comment = { comment: comment }
      comment

    isValid: (comment) ->
      comment? && !_.isBlank(comment.comment)

    addComment: (comment) ->
      comment = @formatComment(comment)
      if @isValid comment
        $http.post("/thanks/#{@id}/comments", {
          "comment": comment
        }).success (data) =>
          @comments.push data
          @hasDirtyComment = false
          @newComment = ''
        .error (data) =>
          @resetErrors _.map data.errors, (err, key) -> stringUtil.formatError key, err

  return Thank
]
