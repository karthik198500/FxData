package com.fxdata.rabbitmqservice;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public class RabbitMqServiceApplication {

    public static void main(String[] args) {
       log.info("This is application starting");
        SpringApplication.run(RabbitMqServiceApplication.class, args);
    }

}
