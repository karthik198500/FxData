package com.fxdata.fxratesmonitor.notify;


import com.fxdata.fxratesmonitor.dto.NotificationDTO;
import reactor.core.publisher.Mono;

public interface NotificationService {

    Mono<String> sendNotification(NotificationDTO notificationDTO);

    Mono<String> sendNotificationWithBody(String body);
}
