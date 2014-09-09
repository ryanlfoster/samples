
window.SLATE.filter 'listAccounts', ->

  pluralize = (num, singular, plural) ->
    if num == 1 then singular else plural

  (accounts, options={}) ->
    options.nameCount ?= 2
    nameCount = options.nameCount

    options.excludes ?= []
    excludes = options.excludes

    if _.isArray accounts
      accounts = _.difference(accounts, excludes)
      nameDelimiter = if accounts.length == 2 then " and " else ", "

      nameList = _(accounts[0..(nameCount - 1)]).map (acct) ->
        acct.display_name

      nameList = nameList.join(nameDelimiter)

      if accounts.length > nameCount
        numOthers = accounts.length - nameCount
        pronoun = pluralize(numOthers, "other", "others")
        nameList += " and #{numOthers} #{pronoun}"

      nameList
    else
      accounts.display_name if accounts?




