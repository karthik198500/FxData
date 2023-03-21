package com.fxdata.fxratesmonitor.engine;

import com.fxdata.fxratesmonitor.config.EhsConfiguration;
import com.fxdata.fxratesmonitor.dto.ForexRateMinDTO;
import com.fxdata.fxratesmonitor.httpclient.EhsWebClientBuilder;
import com.fxdata.fxratesmonitor.notify.NotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Log4j2
public class FxRatesEngine {

    private static final String API_TOKEN = "api_token";
    private static final String FORMAT = "fmt";
    private final EhsWebClientBuilder ehsWebClientBuilder;
    private final EhsConfiguration ehsConfiguration;
    private final NotificationService notificationService;
    private final TopicSender topicSender;

    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(2);
    private final List<ForexRateMinDTO> previousForexRateMinDTOList = new ArrayList<>();



    public FxRatesEngine(EhsWebClientBuilder ehsWebClientBuilder, EhsConfiguration ehsConfiguration, NotificationService notificationService, TopicSender topicSender) {
        this.ehsWebClientBuilder = ehsWebClientBuilder;
        this.ehsConfiguration = ehsConfiguration;
        this.notificationService = notificationService;
        this.topicSender = topicSender;
    }


    @PostConstruct
    public void init(){
        //https://eodhistoricaldata.com/api/real-time/EUR.FOREX?api_token=demo&fmt=json
        // Call this service every minute.
        scheduledExecutorService.scheduleWithFixedDelay(this::fetchAndProcessForexData, 10, 60, TimeUnit.SECONDS );
    }

    public Mono<List<Object>> fetchAndProcessForexData(){
        try {
            Map<String, String> variableMap = new HashMap<>();
            variableMap.put(API_TOKEN, ehsConfiguration.getApiToken());
            WebClient webClient = ehsWebClientBuilder.build(ehsConfiguration.getUrl());

            List<String> forexTypes = ehsConfiguration.getForexTypes();
            List<Mono<ForexRateMinDTO>> monoList = new ArrayList<>();

            for(String forexType: forexTypes){
                log.info("Querying forex type "+forexType);
                monoList.add(getForexData(webClient, forexType));
            }
            Mono<List<Object>> zip = Mono.zip(monoList, listOfResults -> Arrays.stream(listOfResults)
                    .collect(Collectors.toList()));
            zip.log()
                    .subscribe(
                            this::processRawForexData,
                            this::handleException,
                            FxRatesEngine::handleSuccess);
            return zip;
        } catch (Exception exception) {
            handleException(exception);
        }
        return null;
    }

    private void processRawForexData(List<Object> listOfForexData) {
        List<ForexRateMinDTO> forexRateMinDTOList = convertToDTOList(listOfForexData);
        if(previousForexRateMinDTOList.isEmpty()){
            log.info("In the zip method. List of Forex Data is new, so send to topic.");
            if(!forexRateMinDTOList.isEmpty()){
                previousForexRateMinDTOList.clear();
                previousForexRateMinDTOList.addAll(forexRateMinDTOList);
            }
            sendToTopic(forexRateMinDTOList);
        }else if(notSame(forexRateMinDTOList)){
            log.info("In the zip method. List of Forex Data is different from the existing data, so send to topic.");
            //Send to Rabbit MQ.
            sendToTopic(forexRateMinDTOList);
            previousForexRateMinDTOList.clear();
            previousForexRateMinDTOList.addAll(forexRateMinDTOList);
        }else{
            sendToTopic(forexRateMinDTOList);
            //do not do anything.
            log.info("In the zip method. List of Forex Data matches with previous data. So ignoring.");
        }
    }

    private void handleException(Throwable throwable) {
        log.error("Error when executing the queries to EHS Site. Send to notification service "+ throwable.getMessage());
        notificationService.sendNotificationWithBody(throwable.getMessage());
    }

    private static void handleSuccess() {
        log.info("Successfully completed the query to the Ehs Site.");
    }

    private void handleException(Exception exception) {
        log.error("Error in the application. "+ exception.getMessage(), exception);
        notificationService.sendNotificationWithBody(exception.getMessage());
        scheduledExecutorService.shutdown();
    }
    private void sendToTopic(List<ForexRateMinDTO> forexRateMinDTOList) {
        try {
            topicSender.send(forexRateMinDTOList);
        } catch (Exception e) {
            log.error("Error while sending message to topic",e);
            throw new RuntimeException(e);
        }
        forexRateMinDTOList.stream()
                .forEach(log::info);
    }

    private static List<ForexRateMinDTO> convertToDTOList(List<Object> listOfForexData) {
        return listOfForexData.stream()
                .filter(Objects::nonNull)
                .filter(obj -> obj instanceof ForexRateMinDTO)
                .map(obj -> (ForexRateMinDTO) obj)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param listOfForexData
     * @return
     */
    private boolean notSame(List<ForexRateMinDTO> listOfForexData) {
        return listOfForexData.stream()
                .anyMatch(forexRateMinDTO -> !previousForexRateMinDTOList.contains(forexRateMinDTO));

    }

    private Mono<ForexRateMinDTO> getForexData(WebClient webClient, String forexType) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/" + forexType)
                        .queryParam(API_TOKEN, ehsConfiguration.getApiToken())
                        .queryParam(FORMAT, ehsConfiguration.getFmt())
                        .build())
                .retrieve()
                .bodyToMono(ForexRateMinDTO.class);
    }
}
