# Mixpanel Provider
class MixpanelProvider
  defaults: {}

  constructor: (options={})->
    _.extend @, @defaults, options

  track: (eventName, params={})->
    mixpanel.track(eventName, params) if window.mixpanel?


class GoogleAnalyticsProvider
  defaults: {}

  constructor: (options={})->
    _.extend @, @defaults, options

  track: (eventName, params={})->
    if window._gaq?
      _gaq.push([ '_trackEvent', params.category || 'All', eventName ])


# Analytics Service
module = angular.module 'analytics', []
module.service 'analytics', ['$rootScope', ($rootScope)->

  # init providers
  providers = []

  providers.push new MixpanelProvider {}
  providers.push new GoogleAnalyticsProvider {}


  # event binding on root scope

  # use `$scope.$emit('analytics:track', 'eventname')`
  $rootScope.$on 'analytics:track', (e, eventName, params={})->
    console.log 'analytics:track:' + eventName
    _.each providers, (p)->
      p.track(eventName, params) if _.isFunction p.track


  # TODO: intercept window.onerror to log javascript errors
  # this isn't a good place to do it since javasript errors can
  # and will break the execution of this code

  # TODO: write provider for javascript debugging to the console that
  # runs in a debug mode - should be available in dev and prod environment

]
