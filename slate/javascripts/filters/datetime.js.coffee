SLATE.filter 'humanizeTimeFromNow', ->

  secondsFromNow = (date)->
    millis = (new XDate(true)).getTime() - date.getTime()
    Math.abs millis / 1000

  (dateString)->
    if dateString?
      second = 1
      minute = second * 60
      hour = minute * 60
      day = hour * 24
      week = day * 7

      date = new XDate(dateString, true)
      secs = secondsFromNow(date)

      output = null

      switch true
        when secs < minute then output = "#{ parseInt(secs, 10) }s"
        when minute <= secs < hour then output = "#{ parseInt(secs/minute, 10) }m"
        when hour <= secs < day then output = "#{ parseInt(secs/hour, 10) }h"
        when day <= secs < week then output = "#{ parseInt(secs/day, 10) }d"

      output ?= "#{ parseInt(secs/week, 10) }w"
