package com.fxdata.fxeventsmonitor.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fxdata.fxeventsmonitor.dto.ForexRateMinDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class FxEventListener {

    private static final String FOREX_RATE_CHANGE_QUEUE = "q.forex-rate-change-queue";
    private final FxEventsEngine fxEventsEngine;

    public FxEventListener(FxEventsEngine fxEventsEngine) {
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
