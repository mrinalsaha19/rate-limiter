#                                                               DESIGN
<img width="900" height="566" alt="image" src="https://github.com/user-attachments/assets/debbef11-9ee1-484f-8f49-f7c6469bace7" />



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
