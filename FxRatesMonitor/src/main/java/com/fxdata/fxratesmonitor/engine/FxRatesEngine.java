package com.fxdata.fxratesmonitor.engine;

import com.fxdata.fxratesmonitor.config.EhsConfiguration;
import com.fxdata.fxratesmonitor.httpclient.EhsWebClientBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class FxRatesEngine {

    private static final String API_TOKEN = "api_token";
    private static final String FORMAT = "fmt";
    private final EhsWebClientBuilder ehsWebClientBuilder;
    private final EhsConfiguration ehsConfiguration;
    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(2);


    public FxRatesEngine(EhsWebClientBuilder ehsWebClientBuilder, EhsConfiguration ehsConfiguration) {
        this.ehsWebClientBuilder = ehsWebClientBuilder;
        this.ehsConfiguration = ehsConfiguration;
    }


    @PostConstruct
    public void init(){
        //https://eodhistoricaldata.com/api/real-time/EUR.FOREX?api_token=demo&fmt=json
        scheduledExecutorService.scheduleWithFixedDelay(this::run, 10, 60, TimeUnit.SECONDS );
    }

    public void run(){
        try {
            Map<String, String> variableMap = new HashMap<>();
            variableMap.put(API_TOKEN, ehsConfiguration.getApiToken());
            WebClient webClient = ehsWebClientBuilder.build(ehsConfiguration.getUrl());

            List<String> forexTypes = ehsConfiguration.getForexTypes();
            List<Mono<String>> monoList = new ArrayList<>();

            for(String forexType: forexTypes){
                log.info("Querying forex type "+forexType);
                Mono<String> stringMono = invokeEhsServiceForForex(webClient, forexType);
                monoList.add(stringMono);
            }
            Mono.zip(monoList, listOfResults -> {
                        Arrays.stream(listOfResults)
                                .forEach(log::info);
                        return true;
                    }).log()
                    .subscribe(aBoolean -> {
                        log.info("Successfully executed the entire queries");
                    }, throwable -> {
                        log.error("Error when executing the queries "+throwable.getMessage());
                    }, () -> {
                        log.info("Successfully complete consumer.");
                    });
        } catch (Exception exception) {
            log.error("Error in the application. "+exception.getMessage(),exception );
            scheduledExecutorService.shutdown();
        }
    }

    private Mono<String> invokeEhsServiceForForex(WebClient webClient, String forexType) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/" + forexType)
                        .queryParam(API_TOKEN, ehsConfiguration.getApiToken())
                        .queryParam(FORMAT, ehsConfiguration.getFmt())
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
