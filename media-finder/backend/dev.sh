echo "click on the browser window"
window=$(xdotool selectwindow)
while true; do
  node main.js &
  pid=$!
  tries=100
  res=1
  while [ $res -ne 0 ] && [ $tries -gt 0 ]; do
    curl localhost:3000 >/dev/null 2>&1
    res=$?
    tries=$(( $tries - 1 ))
  done
  if [ $tries -eq 0 ]; then
    echo Server is down!
  fi
  current=$(xdotool getwindowfocus)
  xdotool windowfocus $window key F5 windowfocus $current
  inotifywait -rqq -e modify ../frontend ../backend
  kill $pid
done;
