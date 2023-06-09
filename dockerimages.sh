phome=$PWD

cd $phome
docker ps -a
docker images

docker image rm -f fxrabbitmqserviece:0.1
docker image rm -f fxnotificationservice:0.1
docker image rm -f fxratesmonitor:0.1
docker image rm -f fxeventsmonitor:0.1


cd $phome/RabbitMQService
./gradlew build
docker build -t fxrabbitmqserviece:0.1 .

cd $phome/NotificationService
./gradlew build
docker build -t fxnotificationservice:0.1 .

cd $phome/FxRatesMonitor
./gradlew build
docker build -t fxratesmonitor:0.1 .

cd $phome/FxEventsMonitor
./gradlew build
docker build -t fxeventsmonitor:0.1 .

cd $phome
docker images
#docker-compose up

#fxdata-fxratesmonitor-1
#fxdata-fxeventsmonitor-1
#fxdata-fxnotificationservice-1
#fxdata-fxrabbitmqservice-1
#fxdata-rabbitmq-1
#spring-boot-amqp-messaging-rabbitmq-1

