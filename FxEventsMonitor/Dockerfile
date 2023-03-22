FROM openjdk:8-jdk-alpine
RUN mkdir -p /filestore
VOLUME /tmp
ADD build/libs/FxEventsMonitor-0.0.1-SNAPSHOT.jar fxeventsmonitor.jar
ENTRYPOINT ["java","-jar","/fxeventsmonitor.jar"]
