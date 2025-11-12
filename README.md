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

#  Note
Its recomended to have 6 nodes for redis cluster with 3 master and 3 replicas for each master to achieve HA and resilient system.
By default redis requires 3 master nodes under the redis claster. Redis uses Quorum based agreement machanism to heal up a node when its not reachable. 
All Redis nodes are connected to eachother and they keep checking health of eachother with simple ping. When a node is not reachable by another node it marks the node as PFAIL(possible fail)
and it broadcasts the message to the other nodes. The other nodes also do the same check and if majority nodes can not reach to a particular node then its marked as FAIL. 
The node is marked as PFAIL to FAIL and its treated a dead in the cluster and therefore atleast 3 nodes are required to come out of quorum based agreement to replicate the dead node.

