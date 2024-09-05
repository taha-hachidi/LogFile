#!/bin/bash
while true; do
  inotifywait -e modify /app/logs/FINEA_TRACE-2022-11-23-1.log
  echo "File modified!"
done
