define(['util', './utils', './logger'],
	function (util,  utils,		LOG){
    var config = utils.setupConfig();
    function log(){
        LOG.info(arguments);
        var text = util.format.apply(null, arguments);
        LOG.info(text);
    }

    function defaultUser(){
        if (config.bypassWam === true){
            return config.bypassUser;
        } else {
            return "";
        }
    }

    function is_empty(obj) {
        var hasOwnProperty = Object.prototype.hasOwnProperty;
        // Assume if it has a length property with a non-zero value
        // that that property is correct.
        if (obj.length && obj.length > 0)    return false;
        if (obj.length && obj.length === 0)  return true;

        for (var key in obj) {
            if (hasOwnProperty.call(obj, key))    return false;
        }

        return true;
    }

    function addZero(n){
        return n < 10 ? '0' + n: '' + n;
    }

    return {
        log: log,
        defaultUser: defaultUser,
        is_empty: is_empty,
        addZero: addZero
    };
});