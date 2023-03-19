package com.fxdata.fxeventsmonitor.notify;

import com.fxdata.fxeventsmonitor.FxEventsMonitorApplication;
import com.fxdata.fxeventsmonitor.dto.NotificationDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = FxEventsMonitorApplication.class)
@TestPropertySource(locations = {"classpath:application-test.properties"})
class EmailNotificationServiceTest {

    @Autowired
    EmailNotificationService emailNotificationService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void sendNotification() {
        emailNotificationService.sendNotification(NotificationDTO.builder()
                .fromAddress("")
                .toAddress("")
                .subject("")
                .attachmentLocation("")
                .type("")
                .build());
    }
}