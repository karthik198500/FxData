package com.fxdata.fxeventsmonitor.notify;

import com.fxdata.fxeventsmonitor.dto.NotificationDTO;
import reactor.core.publisher.Mono;

public interface NotificationService {

    Mono<String> sendNotification(NotificationDTO notificationDTO);
}
