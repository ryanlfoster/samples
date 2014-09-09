
# Underscore.string setup
_.mixin _.str.exports()

window.SLATE.service 'stringUtil', [ ->

  @slugify = (slug) ->
    if slug?
      slug = slug.toLowerCase()
      slug = slug.split(' ').join('-')
      slug = slug.replace(/[^0-9A-Za-z\-]/, '')
      slug

  @formatError = (key, err) ->
    "#{key[0].toUpperCase()}#{key.slice(1)} #{err}"

]