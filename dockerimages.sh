cd /Users/kkasiraju/dev/MyMusings/FxData
docker ps -a
docker images

docker image rm -f fxrabbitmqserviece:0.1
docker image rm -f fxnotificationservice:0.1
docker image rm -f fxratesmonitor:0.1
docker image rm -f fxeventsmonitor:0.1


cd /Users/kkasiraju/dev/MyMusings/FxData/RabbitMQService
./gradlew build
docker build -t fxrabbitmqserviece:0.1 .

cd /Users/kkasiraju/dev/MyMusings/FxData/NotificationService
./gradlew build
docker build -t fxnotificationservice:0.1 .

cd /Users/kkasiraju/dev/MyMusings/FxData/FxRatesMonitor
./gradlew build
docker build -t fxratesmonitor:0.1 .

cd /Users/kkasiraju/dev/MyMusings/FxData/FxEventsMonitor
./gradlew build
docker build -t fxeventsmonitor:0.1 .

cd /Users/kkasiraju/dev/MyMusings/FxData/
docker images
#docker-compose up

#fxdata-fxratesmonitor-1
#fxdata-fxeventsmonitor-1
#fxdata-fxnotificationservice-1
#fxdata-fxrabbitmqservice-1
#fxdata-rabbitmq-1
#spring-boot-amqp-messaging-rabbitmq-1

