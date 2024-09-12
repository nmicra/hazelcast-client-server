# hazelcast-spring-boot-client-server-example
Demonstrates how to setup hazelcast client-server configuration + hazelcast mamagement.

### Build hazelcast-server
1. `cd hazelcast-server`
2. `gradle build -x test`
3. `docker build -t hzservertest .`

### Build hazelcast-client
1. `cd hazelcast-client`
2. `gradle build -x test`
3. `docker build -t hzclienttest .`

### Run Docker-Compose
`docker-compose up -d`

### Access Clients with swagger
client-1
`http://localhost:8383/swagger-ui/index.html`
client-2
`http://localhost:8384/swagger-ui/index.html`

### Access Hazelcast-Management
http://localhost:8081
add new cluster:
![new-cluster](https://raw.githubusercontent.com/nmicra/hazelcast-client-server/main/hz-management.png)
