
window.SLATE.filter 'humanizeLength', ->

  (num)->
    output = "default"
    switch true
      when 1 <= num <= 23 then output = "tiny"
      when 24 <= num <= 59 then output = "short"
      when 60 <= num <= 119 then output = "medium"
      when 120 <= num <= 199 then output = "long"
    output
