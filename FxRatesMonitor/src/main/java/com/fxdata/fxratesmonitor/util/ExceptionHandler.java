package com.fxdata.fxratesmonitor.util;

import com.fxdata.fxratesmonitor.notify.NotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class ExceptionHandler {

    private final NotificationService notificationService;

    public ExceptionHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void handleException(Throwable throwable) {
        log.error("Error when executing the queries to EHS Site. Send to notification service "+ throwable.getMessage());
        Mono<String> result = notificationService.sendNotificationWithBody(throwable.getMessage());
        result.subscribe(res -> {
            log.info("Successfully sent message to notification service"+res);
        }, e -> log.error("Error sending message to notification service.",e));
    }
}
