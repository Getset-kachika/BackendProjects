FROM openjdk:21
ADD build/libs/LogIngestionService-0.0.1-SNAPSHOT.jar LogIngestionService1.jar
ENTRYPOINT ["java","-jar","LogIngestionService1.jar"]
