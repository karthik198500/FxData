package com.fxdata.fxeventsmonitor.notify;

import com.fxdata.fxeventsmonitor.config.FxEventsConfig;
import com.fxdata.fxeventsmonitor.config.WebClientConfig;
import com.fxdata.fxeventsmonitor.dto.NotificationDTO;
import com.fxdata.fxeventsmonitor.httpclient.FxWebClientBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Service
public class EmailNotificationService implements NotificationService {


    private final FxWebClientBuilder fxWebClientBuilder;
    private final FxEventsConfig fxEventsConfig;

    public EmailNotificationService(FxWebClientBuilder fxWebClientBuilder, FxEventsConfig fxEventsConfig) {
        this.fxWebClientBuilder = fxWebClientBuilder;
        this.fxEventsConfig = fxEventsConfig;
    }

    @Override
    public Mono<String> sendNotification(NotificationDTO notificationDTO) {
        WebClient webClient = fxWebClientBuilder.build(fxEventsConfig.getNotification().getServiceUrl());
        Mono<String> stringMono = webClient.post()
                .body(Mono.just(notificationDTO), NotificationDTO.class)
                .retrieve()
                .bodyToMono(String.class);
        return stringMono;
    }
}
