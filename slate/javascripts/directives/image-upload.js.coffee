SLATE.directive 'imageUpload', ->
  'use strict'

  findOrCreateCanvas = ->
    canvasId = 'imageUpload-resize-canvas'
    canvas = $(canvasId)

    unless canvas[0]
      canvas = $("<canvas id='#{canvasId}'></canvas>")
        .css 'visibility', 'hidden'
      $('body').append canvas

    canvas[0]

  # TODO: crop like paperclip does
  resizeImage = (orig, options, callback)->
    options.maxH ?= 300
    options.maxW ?= 300
    options.quality ?= .7

    canvas = findOrCreateCanvas()
    ctx = canvas.getContext '2d'

    h = orig.height
    w = orig.width

    if w > h
      if w > options.maxW
        h = Math.round h *= options.maxW / w
        w = options.maxW
    else
      if h > options.maxH
        w = Math.round w *= options.maxH / h
        h = options.maxH

    canvas.width = w
    canvas.height = h

    ctx.drawImage orig, 0, 0, w, h
    canvas.toBlob callback, 'image/jpeg', options.quality

  return {
    restrict: 'AE'
    replace: true
    scope:
      inputName: '@'
      fallbackImagePath: '@'
      imgOriginal: '='
      imgResized: '='
    template: """
      <div>
        <input name="{{inputName}}" type="file">
      </div>
    """
    link: (scope, element, attrs)->
      image = scope.image = {}

      reader = if FileReader? then new FileReader() else false

      reader.onload = (evt)->
        imgOriginalBlob = new Blob [new Int8Array(evt.target.result)], { type: image.type }

        _.extend imgOriginalBlob,
          url: URL.createObjectURL imgOriginalBlob
          name: image.name

        imgOriginal = new Image()
        imgOriginal.onload = ->
          resizeOpts = {}
          resizeImage imgOriginal, resizeOpts, (imgResizedBlob)->
            _.extend imgResizedBlob,
              url: URL.createObjectURL imgResizedBlob
              name: image.name

            scope.$apply ->
              scope.imgOriginal = imgOriginalBlob
              scope.imgResized = imgResizedBlob

        imgOriginal.src = imgOriginalBlob.url


      element.bind 'change', (evt)->
        if reader
          image = evt.target.files[0]
          return unless image
          return unless image.type.match 'image.*'
          reader.readAsArrayBuffer image
        else
          scope.imgOriginal =
            url: scope.fallbackImagePath
          scope.imgResized =
            url: scope.fallbackImagePath
  }
