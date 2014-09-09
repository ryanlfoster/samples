define(["handlebars"], function(){
  //do not display an empty object
  Handlebars.registerHelper('noEmpty', function(field){
    var isEmptyObject = field instanceof Object && Object.keys(field).length === 0 || typeof field == 'undefined';
    return isEmptyObject ? "" : field;
  });

  //only take the first n chars
  Handlebars.registerHelper('nChars', function(field, n, def, render){
    def = (def === undefined) ? field.toString : def;
    field = (typeof field !== "string" || field === undefined) ? def : field;
    return (field.length < n) ? field : field.substr(0, n - 3) + '...';
  });

  //iterate through an object as key-value pairs
  Handlebars.registerHelper('kvpairs', function(context, render){
    var key, res = [];
    for (key in context) if (context.hasOwnProperty(key)){
      res.push(render({
        key: key,
        val: context[key]
      }));
    }
    return res.join('');
  });

  //display translated from Camel Case
  Handlebars.registerHelper('unCamel', function(field, render){
    return field[0].toUpperCase() + field.slice(1).replace(/([A-Z])/, " $1");
  });

  //display but with spaces instead of +
  Handlebars.registerHelper('unPlus', function(field, render){
    return field.replace(/\+/g, ' ');
  });

  //take the first that is not undefined
  Handlebars.registerHelper('ifEq', function(member, target, context, render){
    if (member === target){
      return render(context);
    }
  });

  //take the first that is not undefined
  Handlebars.registerHelper('firstOf', function(){
    var result, args = Array.prototype.slice.call(arguments);
    while (result === undefined && args.length) {
      result = args.shift();
    }
    return result;
  });

  //take the first that is not undefined
  Handlebars.registerHelper('json', function(attr){
    return JSON.stringify(attr, null, "   ");
  });

  //translate 'true' and 'false' to 'yes' and'no'
  Handlebars.registerHelper('trueFalseToYesNo', function(attr){
    return (attr === "true") ? 'Yes' : 'No';
  });

  Handlebars.registerHelper('uppercase', function(field){
    return field.toUpperCase();
  });

  //Loop through special delimiters
  Handlebars.registerHelper('delimit', function(context, delimiter, block){
    if(typeof context !== "undefined"){
      var loop = context.split(delimiter),
          html = '';

      for(var i=0; i < loop.length; i++){
        html += block(loop[i]);
      }
      return html;
    }
    return null;
  });

  //construct a link that is relative to the path of current page
  Handlebars.registerHelper('Details-relLink', function(id, br){
    var link = window.location.protocol + "//" + window.location.host + "/entry/" + id;
    if (br === "true"){
      var oneThird = Math.ceil(link.length / 3);
      var twoThirds = oneThird * 2;
      var first = link.substring(0, oneThird);
      var second = link.substring(oneThird, twoThirds);
      var third = link.substring(twoThirds);

      link = [first,second,third].join(" <br/> ");
    }

    return link;
  });

  Handlebars.registerHelper('ifFirst', function(first, second){
    if(typeof first !== 'undefined' && first !== null && first.trim() !== ""){
      return first;
    }else{
      return second;
    }
  });


  Handlebars.registerHelper('ifCond', function(v1, v2, options) {
    if(v1 == v2) {
      return options.fn(this);
    }
    return options.inverse(this);
  });

  Handlebars.registerHelper('dateFormat', function(context, format) {
    var shortDate = context['#'].split(' ')[0];
    var date = new Date(Date.parseExact(shortDate, 'yyyy-MM-dd'));
    return date.toString(format);
  });

  Handlebars.registerHelper('requestIsApproved', function(context, entryId, block) {
    if(context === "Approved"){
      var attr = 'class=showAsset data-assetId=' + entryId;
      return attr;
    }
    return null;
  });

  Handlebars.registerHelper('fixupTitle', function(title){
    if (title instanceof Object || title === undefined) {
      title = "No Title";
    }

    var parts = /^[\[\|]*([^\|\]]*)[\]\|]*$/.exec(title);
    if (parts !== null && parts[1] !== undefined){
      title = parts[1];
    }

    if (title === "||" || title === ""){
      title = "No Title";
    }

    return title;
  });

});