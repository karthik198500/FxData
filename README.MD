
High level Design
![Design.png](Design.png)

Sign up for
https://mailosaur.com/app/signup/
Configure the below from the SMTP & POP3 credential after logging in.
/FxData/NotificationService/src/main/resources/application.properties
spring.mail.username=avbpwlvj@mailosaur.net
spring.mail.password=3B3Vo3ltLTE428mL

Running the application
Download code from git 
In the root folder run which creates all docker images.
sh dockerimages.sh

Run the docker-compose up based on profiles.

- docker-compose --profile core up
  - Wait
- docker-compose --profile rabbitmq up
  - Run http://localhost:8010/actuator/health on browser and check the health status - up
  - Run http://localhost:15672/#/queues to check if queues are created successfully.
  - Wait
- docker-compose --profile service up
  - Wait
- docker-compose --profile consumer up

check https://mailosaur.com/app/ with your username/password to check for mail message with CSV attachment.

End result
![img.png](img.png)

Major Design concepts
- Not blocking
- Reactive nature.
- Controlled through configuration.
  - Http connect timeout, socket timeout.

There are total five docker instances in the whole application.

RabbitMQ
  - Rabbit MQ.
RabbitMQService
Contains all configuration related to Rabbit MQ
- Queue creation
- Topic Creation
- Exchange creation 

FxRatesMonitor
- Rates Engine
  - EHS Query process
    - I have checked responses from the EHSHistoricalData and its changing every minute.
    - Wrote a scheduler which queries all the list of Forex Data of interest every one minute.
    - Query the EHS historical site independently.
    - Used reactor framework which is follow reactive principles and can scale well
  - Combine Data
    - Combine the responses from all the forex data and compare with the previous data
  - Push to Rabbit Queue on the fxevent exchanger.
  - Any exception/error during this process send a notification message using notification service developers about the exception.
  - Control the list of forexs through configuration
  - Control the notification data (from address, to address everything through configuration)

FXEvents Monitor
- Events Engine
  - Events Reader
    - Read from Queue - 
    - Keep it independent so that we can change implementation later if needed  
  - Process Information
    - process the queue data and change if required
    - Write to CSV - Used Open CSV  
    - Can plug and play any another service here
  - Send to notification service
    - Populate notification data
    - Send to notification service with the location of the attachment

Notification Service
- Rest service
- State less
- Scaled independently
- We persist the incoming data, in case we want reliability.
- We enhance this service to support other modes of delivery
    - sms
    - slack
- Request data is JSON and change content as required.
- Separation of concerns and single responsibility principles
- SOLID
- Global exception handler in rest service to handle all errors.
- POST service - Not Idempotent.


List of enhancements 
- Validate the Forex configuration by retrieving the list of forexes supported and checking them.
- We can use Rabbit MQ for notification messages, since i felt its much better suitable for scalability
- Do you think kafka can work better instead of MQ 
  - Scalability
  - Back pressure
   https://doordash.engineering/2020/09/03/eliminating-task-processing-outages-with-kafka/
- Persistence of messages across various layers. RabbitMQ and notification service to improve reliability
- Using Orchestration framework like Kubernetes to manage the various services.
- Acknowledgment of various steps across the services to make sure steps are followed.
- If EHSHistoricalData support push mechanism instead of pooling it would improve performance.Probably web hooks
- If more messages and types needs to supported we can use the further topics and queues to handle data appropriately.
- Write a common library which can be used across these four modules.
- Jackson converter using a common library.
- Introduce Spring Retry mechanism where required.
- We can use logback SMTP log to send emails.

For each service we can always think about a data store 
- Write to DB and use a CQRS mechanism
  - Only service will write to DB
  - Lot of services will act on the DB to process the data.

When ever there is too much data we think about
- Rate limiting
- Circuit breaker pattern
- Bulkhead pattern - One service down should not impact other services.
- 

Other enhancements
- Reuse webclients
- Websockets

RabbitMQ pushes events to events monitor
- Acknowledgment mechanism
- Back pressure
- parallel processing

Events Monitor
If data increase we can use the 
- For example divide data based on the geography.
- we can write various exchange names and divide data based on that
- We can write lambda

Have a data store at each layer
- Send data to and control it
- Circuit breaker pattern
- Rate limiting
- Leaky bucket algorithm
- 12 Factor App for microservices
  - 


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


