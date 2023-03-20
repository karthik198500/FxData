package com.fxdata.fxratesmonitor.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fxdata.fxratesmonitor.dto.ForexRateMinDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class TopicSender {

    private static final String EMAIL_FX_EVENT_RATECHANGE = "email.fxevent.ratechange";
    private final RabbitTemplate rabbitTemplate;
    @Value("${queue.name}")
    private String queueName;

    public TopicSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(List<ForexRateMinDTO> forexRateMinDTOList) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(forexRateMinDTOList);
            log.info("Sending data in json format"+jsonData);
            rabbitTemplate.convertAndSend(queueName, EMAIL_FX_EVENT_RATECHANGE, jsonData);
        } catch (JsonProcessingException e) {
            log.error("Error while converting forex data to json",e);
            throw new RuntimeException(e);
        }

    }


}
