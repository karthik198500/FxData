
High level Design
![Design.png](Design.png)

Design Principles
SOLID principles


Testing 
Used MockWebServer for testing the notification service


Repositories
We can use a separate repository for each service
Probably use git submodules?

Notification Service
Kafka vs RabbitMQ
Better to use Kafka

Docker container



Design
Websocket connection from the spring server to all the different rest services.
Collect the data for every one min in a asynchronous way.
check if the data has changes from previous and send only if the content has changed.


