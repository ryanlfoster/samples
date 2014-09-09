#!/bin/sh
echo $@ >backend/buildargs.txt
cp backend/config/$1.json backend/config.json
echo "building LESS"
lessc -x frontend/css/main.less > frontend/css/main.css

# node config.js
# ----------
# var data = JSON.parse(require('config/dev.json'));
# var template = Handlebars.compile(fs.readFileSync('app.build.js'));
# fs.writeFileSync('app.build-with-config.js', template(data));

# node node_modules/openweb-build/bin/build.js


echo someday requireJS here

#echo "requireJS"
#node r.js -o mf.build.js
#echo "build done"