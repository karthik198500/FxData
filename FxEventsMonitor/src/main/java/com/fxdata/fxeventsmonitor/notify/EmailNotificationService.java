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
    private WebClient webClient;

    public EmailNotificationService(FxWebClientBuilder fxWebClientBuilder, FxEventsConfig fxEventsConfig) {
        this.fxWebClientBuilder = fxWebClientBuilder;
        this.fxEventsConfig = fxEventsConfig;
    }

    public synchronized void initializeWebClient(String url){
        if(null!=url){
            webClient = fxWebClientBuilder.build(url);
        }else{
            webClient = fxWebClientBuilder.build(fxEventsConfig.getNotification().getServiceUrl());
        }

    }


    @Override
    public Mono<String> sendNotification(NotificationDTO notificationDTO) {
        if(webClient == null){
            initializeWebClient(null);
        }
        Mono<String> stringMono = webClient.post()
                .body(Mono.just(notificationDTO), NotificationDTO.class)
                .retrieve()
                .bodyToMono(String.class);
        return stringMono;
    }
}
