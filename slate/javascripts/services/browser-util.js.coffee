
window.SLATE.service 'browserUtil', [ ->

  userAgent = navigator.userAgent

  @isIE = ->
    /MSIE (\d+\.\d+);/.test userAgent
    

]