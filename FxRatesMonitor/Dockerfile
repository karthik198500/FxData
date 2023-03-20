FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD build/libs/FxRatesMonitor-0.0.1-SNAPSHOT.jar fxratesmonitor.jar
ENTRYPOINT ["java","-jar","/fxratesmonitor.jar"]
