SLATE.factory 'Wall', ['$http', 'Thank', ($http, Thank)->

  class Wall
    thanks: []

    url: SLATE._config.routes.wallPath
    isLoading: false
    page: 0

    reset: (thanks=[])->
      _(thanks).each (thank) =>
        @add thank
      @page = 1

    add: (thank=null)->
      return if _.isEmpty(thank) or !thank?

      @thanks.push new Thank thank

    fetch: (reset=false)=>
      @page = 0 if reset
      ++@page
      request = $http.get("#{ @url }", { params: { page: @page } })
      request.success (data)=>
        _(data.thanks).each (thank) =>
          @add thank

      request

  return new Wall
]
