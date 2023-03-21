

API Key
6413c77b46bc25.28701840

Repositories
We can use a separate repository for each service
Probably use git submodules?

Notification Service


Kafka vs RabbitMQ
Better to use Kafka


Design

Websocket connection from the spring server to all the different rest services.
Collect the data for every one min in a asynchronous way.
check if the data has changes from previous.

If yes sent a message to rabbitMQ

Another spring boot service will consume the message and for a csv file and send to
an email address.

docker-compose up -d rabbitmq

##########################################
# Dockerfile to change from root to
# non-root privilege
###########################################
# Base image is CentOS 7
FROM Centos:7
# Add a new user "john" with user id 8877
RUN useradd -u 8877 john
# Change to non-root privilege
USER john
https://www.thegeekdiary.com/run-docker-as-a-non-root-user/


#Pull the base image as Ubuntu
FROM ubuntu:latest

#Add a user with userid 8877 and name nonroot
RUN useradd −u 8877 nonroot

#Run Container as nonroot
USER nonroot



docker system prune -a
delete all images
docker stop $(docker ps -a -q)

delete all containers
docker rm $(docker ps -a -q)

Delete all un-tagged (or intermediate) images:
sudo docker rmi $( sudo docker images | grep '<none>' | tr -s ' ' | cut -d ' ' -f 3)
docker rmi $( docker images | grep '<none>' | tr -s ' ' | cut -d ' ' -f 3)


docker-compose --profile core up
docker-compose --profile rabbitmq up
docker-compose --profile service up
docker-compose --profile consumer up


