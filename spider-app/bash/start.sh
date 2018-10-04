#!/bin/bash
sh stop.sh
echo "current path is ------> $(pwd)"
APP_NAME=spider-app.war
export JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF8
sudo java -Duser.timezone=GMT+8 \
  -Xms128m -Xmx4096m -Xmn128m \
  -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:CMSInitiatingOccupancyFraction=70 \
  -XX:+CMSParallelRemarkEnabled -XX:+CMSClassUnloadingEnabled -XX:SurvivorRatio=8 -XX:+DisableExplicitGC \
  -jar $APP_NAME >/dev/null &
echo $!> ./.pid
echo "===app started!"
sleep 2
tail -100f logs/application.log