This repository contains a sample of communication for OT to IT World.

# Execution
The project can be executed with Docker-Compose :
````
docker-compose up -d
````

# Connection to the OPC Server
Once docker-compose is up, you will be able to connect to the OpcServer with a client. opcua-commander is a good one. More information [https://github.com/node-opcua/opcua-commander](here)

````
opcua-commander -e opc.tcp://localhost:62541/Quickstarts/ReferenceServer
````
