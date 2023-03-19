package com.fxdata.fxeventsmonitor.notify;

import com.fxdata.fxeventsmonitor.config.FxEventsConfig;
import com.fxdata.fxeventsmonitor.config.WebClientConfig;
import com.fxdata.fxeventsmonitor.dto.NotificationDTO;
import com.fxdata.fxeventsmonitor.httpclient.WebClientBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class EmailNotificationService implements NotificationService {


    private final WebClientBuilder webClientBuilder;
    private final FxEventsConfig fxEventsConfig;
    private final WebClientConfig webClientConfig;

    public EmailNotificationService(WebClientBuilder webClientBuilder, FxEventsConfig fxEventsConfig, WebClientConfig webClientConfig) {
        this.webClientBuilder = webClientBuilder;
        this.fxEventsConfig = fxEventsConfig;
        this.webClientConfig = webClientConfig;
    }

    @Override
    public void sendNotification(NotificationDTO notificationDTO) {
        WebClient webClient = webClientBuilder.build(fxEventsConfig.getNotification().getServiceUrl());
        Mono<String> stringMono = webClient.post()
                .body(Mono.just(notificationDTO), NotificationDTO.class)
                .retrieve()
                .bodyToMono(String.class);
    }
}
