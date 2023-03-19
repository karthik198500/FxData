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
