#!/bin/bash
PID=$(cat ./.pid)
sudo kill -TERM $PID
sleep 2
echo "app is stopped!"
ps -ef |grep spider-app