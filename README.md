#                                                               DESIGN
<img width="837" height="522" alt="image" src="https://github.com/user-attachments/assets/ca57bd27-cabe-4d83-9d43-86e158ee4a01" />




# Run Redis
- docker run --name redis-local -p 6379:6379 -d redis:latest
# Run zookeeper and kafka server
# EndPoint to simulate
- In window of 10 seconds, only 5 apis are excepted. The rest goes to a kafka topic for later processing with scheduler.

for i in $(seq 1 7); do
  curl -s -X POST "http://localhost:8080/events?key=user123" \
  -H "Content-Type: application/json" \
  -d "{\"id\": $i}"
  echo
done
