FROM maven:3.8.1-jdk-11 as build
WORKDIR /src
COPY ["src/plc-subscription-source-connector/.", "."]

RUN mvn package -DskipTests

FROM confluentinc/cp-kafka-connect-base AS connect
WORKDIR /usr/share/java
COPY --from=build /src/target/plc-subscription-source-connector-1.0.0.0.dev-SNAPSHOT-jar-with-dependencies.jar .
