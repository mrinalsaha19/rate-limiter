#                                                               DESIGN - Single App Node, Single Redis Cluster
<img width="837" height="522" alt="image" src="https://github.com/user-attachments/assets/ca57bd27-cabe-4d83-9d43-86e158ee4a01" />

#                                                               DESIGN - Multi App Node, Single Redis Cluster
Usage of global redis key to ensure same hashing. This helps acheving global rate limit across multi node. 
<img width="942" height="565" alt="image" src="https://github.com/user-attachments/assets/edd6f6af-8b3f-4518-b809-7eb6825fbf5e" />






# Run Redis
- docker run --name redis-local -p 6379:6379 -d redis:latest
- Run zookeeper and kafka server
# EndPoint to simulate
- In window of 10 seconds, only 200 apis are accepted for processing. The rest are deffered and published to a deffered topic for later processing with scheduler.

for i in $(seq 1 7); do
  curl -s -X POST "http://localhost:8080/events" \
  -H "Content-Type: application/json" \
  -d "{\"id\": $i}"
  echo
done

# REDIS LOCAL SET UP (for dev purpose):
- choco install redis-64
- redis-cli --version
- mkdir C:\redis-cluster\node1\data
  mkdir C:\redis-cluster\node2\data
  mkdir C:\redis-cluster\node3\data
  - C:\redis-cluster\node1\redis.conf
    port 7000
    cluster-enabled yes
    cluster-config-file nodes.conf
    cluster-node-timeout 5000
    appendonly yes
    dbfilename dump.rdb
    dir C:\redis-cluster\node1\data
  - C:\redis-cluster\node2\redis.conf
    port 7001
    cluster-enabled yes
    cluster-config-file nodes.conf
    cluster-node-timeout 5000
    appendonly yes
    dbfilename dump.rdb
    dir C:\redis-cluster\node2\data
  - C:\redis-cluster\node3\redis.conf
    port 7001
    cluster-enabled yes
    cluster-config-file nodes.conf
    cluster-node-timeout 5000
    appendonly yes
    dbfilename dump.rdb
    dir C:\redis-cluster\node2\data
  - Start all the 3 nodes
     cd C:\redis-cluster\node1
     redis-server redis.conf

     cd C:\redis-cluster\node2
     redis-server redis.conf

     cd C:\redis-cluster\node3
     redis-server redis.conf

- create cluster
  redis-cli --cluster create 127.0.0.1:7000 127.0.0.1:7001 127.0.0.1:7002 --cluster-replicas 0


