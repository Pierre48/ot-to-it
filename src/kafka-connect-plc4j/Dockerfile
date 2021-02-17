FROM alpine as base
WORKDIR /src
RUN wget https://apache.mediamirrors.org/plc4x/0.8.0/apache-plc4x-0.8.0-source-release.zip
RUN unzip apache-plc4x-0.8.0-source-release.zip

FROM maven as build
WORKDIR /src
COPY --from=base /src/apache-plc4x-0.8.0/. .
WORKDIR "/src/plc4j/integrations/apache-kafka/"
RUN mvn package  -DskipTests

FROM confluentinc/cp-kafka-connect-base AS connect
WORKDIR /usr/share/java
COPY --from=build /src/plc4j/integrations/apache-kafka/target/plc4j-apache-kafka-0.8.0-uber-jar.jar .
