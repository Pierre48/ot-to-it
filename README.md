This repository contains a sample of communication for OT to IT World.

# Purpose
This project is composed of several projects :
- **OpcServer** : An sample OPC Server with 20 productions machines :
````
RootFolder
├── Factory
    ├── Machine_1
        ├── id_x
        ├── Fabrication_Date_1
        ├── Fabrication_Id_1
        ├── Fabrication_Weight_1
...
    ├── Machine_20
        ├── id_20
        ├── Fabrication_Date_20
        ├── Fabrication_Id_20
        ├── Fabrication_Weight_20
````
- **OpcSimulator** : Simulate production for each machine. It randomly changes Id, Date, and Weight.
- **kafak-connect-plc4j** : Just a dockerfile that allows to build a container image based on kafka connect plus add pl4j lib

The repository provides a docker-compose file that allows to build OpcServer and OpcSimulator, and next run a Kafka cluster, plus image based on each projects.

# Execution
The project can be simply executed with Docker-Compose :
````
docker-compose up -d
````

##  OPC Server tags
Once docker-compose is up, you will be able to connect to the OpcServer with a client. opcua-commander is a good one. More information [https://github.com/node-opcua/opcua-commander](here)

````
opcua-commander -e opc.tcp://localhost:62541/Quickstarts/ReferenceServer
````

## Kafka cluster
Kafka object are viewable with akhq to the following url : [AKHQ](http://localhost:80080)
