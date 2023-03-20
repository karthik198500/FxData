package com.fxdata.fxeventsmonitor.engine;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.JsonParserDelegate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fxdata.fxeventsmonitor.dto.ForexRateMinDTO;
import com.fxdata.fxeventsmonitor.dto.FxRateDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class EventListener {

    private static final String FOREX_RATE_CHANGE_QUEUE = "q.forex-rate-change-queue";
    private final FxEventsEngine fxEventsEngine;

    public EventListener(FxEventsEngine fxEventsEngine) {
        this.fxEventsEngine = fxEventsEngine;
    }

    @RabbitListener(queues = FOREX_RATE_CHANGE_QUEUE)
    public void receive(Message message) {
        log.info("Received message ****"+message.getPayload().toString());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<ForexRateMinDTO> forexRateMinDTOList = objectMapper.readValue(message.getPayload().toString(), new TypeReference<List<ForexRateMinDTO>>(){});
            log.info("After conversion ****"+forexRateMinDTOList);
            fxEventsEngine.sendMessage(forexRateMinDTOList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
