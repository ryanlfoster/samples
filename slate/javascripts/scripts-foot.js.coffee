# This is a manifest file that'll be compiled into application.js, which will include all the files
# listed below.
#
# Any JavaScript/Coffee file within this directory, lib/assets/javascripts, vendor/assets/javascripts,
# or vendor/assets/javascripts of plugins, if any, can be referenced here using a relative path.
#
# It's not advisable to add code directly here, but if you do, it'll appear at the bottom of the
# the compiled file.
#
# WARNING: THE FIRST BLANK LINE MARKS THE END OF WHAT'S TO BE PROCESSED, ANY BLANK LINE SHOULD
# GO AFTER THE REQUIRES BELOW.
#
# Vendor
#= require util
#= require vendor/bootstrap/bootstrap
#= require vendor/jquery.placeholder
#= require vendor/xdate
#= require vendor/fineupload/jquery.fineuploader-3.3.0
#
# App
#= require controllers/slate
#= require config/routes
#
# Controllers
#= require controllers/alert
#= require controllers/wall-ctrl
#= require controllers/thank-new
#= require controllers/thank
#= require controllers/account-avatar
#
# Utils
#= require utils/string
#= require utils/number
#
# Services
#= require services/analytics
#= require services/likeable
#= require services/wall
#= require services/thank
#
# Directives
#= require directives/autogrow
#= require directives/badge-area
#= require directives/comment
#= require directives/keysubmit
#= require directives/placehold
#
# Filters
#= require filters/list-accounts
#= require filters/datetime
#= require filters/humanize-length
#
