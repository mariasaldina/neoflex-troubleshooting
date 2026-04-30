#!/bin/bash

./mvnw clean package

java -jar target/user-*.jar &
APP_PID=$!

echo "Приложение запускается, PID $APP_PID"

until curl -s http://localhost:8080/actuator/health | grep -q "UP"; do
  sleep 1
done

echo "Запускаю запросы..."

PIDS=()

for i in {1..20}; do
  while true; do
    curl -s -X POST http://localhost:8080/user \
      -H "Content-Type: application/json" \
      -d "{\"username\":\"user_$RANDOM\",\"password\":\"12345\"}" > /dev/null
  done &

  PIDS+=($!)
done

sleep 5

echo "Снимаю первый дамп"
jstack $APP_PID > dump1.txt

sleep 10

echo "Снимаю второй дамп"
jstack $APP_PID > dump2.txt

echo "Снимаю дамп памяти"
jcmd $APP_PID GC.heap_dump heap.hprof

echo "Прерываю запросы..."

for pid in "${PIDS[@]}"; do
  kill "$pid" 2>/dev/null
done

kill $APP_PID 2>/dev/null

echo "Готово"