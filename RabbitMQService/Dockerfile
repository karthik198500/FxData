FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD build/libs/RabbitMQService-0.0.1-SNAPSHOT.jar fxrabbitmqservice.jar
ENTRYPOINT ["java","-jar","/fxrabbitmqservice.jar"]
