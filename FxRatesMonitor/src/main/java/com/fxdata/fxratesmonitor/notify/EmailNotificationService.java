package com.fxdata.fxratesmonitor.notify;


import com.fxdata.fxratesmonitor.config.FxEventsConfig;
import com.fxdata.fxratesmonitor.config.WebClientConfig;
import com.fxdata.fxratesmonitor.dto.NotificationDTO;
import com.fxdata.fxratesmonitor.httpclient.FxWebClientBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class EmailNotificationService implements NotificationService {


    private final FxWebClientBuilder fxWebClientBuilder;
    private final FxEventsConfig fxEventsConfig;
    private final WebClientConfig webClientConfig;

    public EmailNotificationService(FxWebClientBuilder fxWebClientBuilder, FxEventsConfig fxEventsConfig, WebClientConfig webClientConfig) {
        this.fxWebClientBuilder = fxWebClientBuilder;
        this.fxEventsConfig = fxEventsConfig;
        this.webClientConfig = webClientConfig;
    }

    @Override
    public void sendNotification(NotificationDTO notificationDTO) {
        WebClient webClient = fxWebClientBuilder.build(fxEventsConfig.getNotification().getServiceUrl());
        Mono<String> stringMono = webClient.post()
                .body(Mono.just(notificationDTO), NotificationDTO.class)
                .retrieve()
                .bodyToMono(String.class);
    }

    @Override
    public void sendNotificationWithBody(String body) {
        FxEventsConfig.Notification notification = fxEventsConfig.getNotification();
        sendNotification(NotificationDTO.builder()
                .type(notification.getType())
                .fromAddress(notification.getFromAddress())
                .toAddress(notification.getToAddress())
                .subject(notification.getSubject())
                .body(body)
                .build());
    }
}