package com.fxdata.fxeventsmonitor.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fxdata.fxeventsmonitor.config.FxEventsConfig;
import com.fxdata.fxeventsmonitor.dto.ForexRateMinDTO;
import com.fxdata.fxeventsmonitor.dto.FxRateDTO;
import com.fxdata.fxeventsmonitor.dto.NotificationDTO;
import com.fxdata.fxeventsmonitor.notify.NotificationService;
import com.fxdata.fxeventsmonitor.writer.FxRateWriter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
@Log4j2
public class FxEventsEngine {

    private final FxRateWriter<FxRateDTO> fxRateWriter;
    private final NotificationService notificationService;
    private final FxEventsConfig fxEventsConfig;

    private Executor cachedThreadPool = Executors.newCachedThreadPool();


    public FxEventsEngine(FxRateWriter<FxRateDTO> fxRateWriter, NotificationService notificationService, FxEventsConfig fxEventsConfig) {
        this.fxRateWriter = fxRateWriter;
        this.notificationService = notificationService;
        this.fxEventsConfig = fxEventsConfig;
    }

    public void sendMessage(List<ForexRateMinDTO> message){
        CompletableFuture.supplyAsync(() -> FxEventsEngine.this.run( message),cachedThreadPool )
                .handle((result, throwable) -> {
                    if(null != result){
                        result.subscribe(s -> log.info(s));
                    }else if( null != throwable) {
                        log.error("Exception while sending message to notification service",throwable);
                    }
                    return Optional.empty();
                });
    }


    public Mono<String> run(List<ForexRateMinDTO> forexRateMinDTOList){
        //Process message from MQ
        List<FxRateDTO> fxRateDTOList = processMessage(forexRateMinDTOList);

        //Writer message to CSV
        String fileLocation = fxRateWriter.write(fxRateDTOList);

        //Send notification
        Mono<String> result = sendNotification(fileLocation);

        return result;
    }

    private List<FxRateDTO> processMessage(List<ForexRateMinDTO> forexRateMinDTOList) {
        return forexRateMinDTOList.stream()
                .map(forexRateMinDTO -> FxRateDTO.builder()
                        .forex(forexRateMinDTO.getCode())
                        .value(forexRateMinDTO.getClose())
                        .build())
                .collect(Collectors.toList());
    }

    private Mono<String> sendNotification(String fileLocation) {
        FxEventsConfig.Notification notificationConfig = fxEventsConfig.getNotification();
        return notificationService.sendNotification(NotificationDTO.builder()
                .type(notificationConfig.getType())
                .fromAddress(notificationConfig.getFromAddress())
                .toAddress(notificationConfig.getToAddress())
                .subject(notificationConfig.getSubject())
                .body(notificationConfig.getBody())
                .attachmentLocation(fileLocation)
                .build());
    }
}
