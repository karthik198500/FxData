package com.fxdata.fxeventsmonitor.engine;

import com.fxdata.fxeventsmonitor.config.FxEventsConfig;
import com.fxdata.fxeventsmonitor.dto.FxRateDTO;
import com.fxdata.fxeventsmonitor.dto.NotificationDTO;
import com.fxdata.fxeventsmonitor.notify.NotificationService;
import com.fxdata.fxeventsmonitor.writer.FxRateWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FxEventsEngine {

    private final FxRateWriter<FxRateDTO> fxRateWriter;
    private final NotificationService notificationService;
    private final FxEventsConfig fxEventsConfig;

    public FxEventsEngine(FxRateWriter<FxRateDTO> fxRateWriter, NotificationService notificationService, FxEventsConfig fxEventsConfig) {
        this.fxRateWriter = fxRateWriter;
        this.notificationService = notificationService;
        this.fxEventsConfig = fxEventsConfig;
    }

    public void run(){
        //Receive message from RabbitMQ

        //Process message from MQ
        List<FxRateDTO> fxRateDTOList = new ArrayList<>();

        //Writer message to CSV
        String fileLocation = fxRateWriter.write(fxRateDTOList);

        //Send notification
        FxEventsConfig.Notification notificationConfig = fxEventsConfig.getNotification();
        notificationService.sendNotification(NotificationDTO.builder()
                        .type(notificationConfig.getType())
                        .fromAddress(notificationConfig.getFromAddress())
                        .toAddress(notificationConfig.getToAddress())
                        .subject(notificationConfig.getSubject())
                        .body(notificationConfig.getBody())
                        .attachmentLocation(fileLocation)
                .build());
    }
}
