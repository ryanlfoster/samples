window.SLATE.service 'numberUtil', [ ->

  @getRandomInt = (min, max) ->
    Math.floor(Math.random() * (max - min + 1)) + min

]