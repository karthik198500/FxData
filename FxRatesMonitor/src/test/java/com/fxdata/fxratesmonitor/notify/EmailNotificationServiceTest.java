package com.fxdata.fxratesmonitor.notify;

import com.fxdata.fxratesmonitor.config.FxEventsConfig;
import com.fxdata.fxratesmonitor.dto.NotificationDTO;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = {"classpath:application-test.properties"})
class EmailNotificationServiceTest {

    @Autowired
    @InjectMocks
    EmailNotificationService emailNotificationService;

    @MockBean
    private FxEventsConfig fxEventsConfig;

    public static MockWebServer mockWebServer;

    @Test
    void contextLoads() {
    }

    @BeforeAll
    static void setUp() throws IOException{
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }


    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s",
                mockWebServer.getPort());
        FxEventsConfig.Notification notification = mock(FxEventsConfig.Notification.class);
        when(fxEventsConfig.getNotification()).thenReturn(notification);
        when(notification.getServiceUrl()).thenReturn(baseUrl);
    }

    @Test
    void sendNotification() {

        String emailResponse = "Successfully sent data";

        mockWebServer.enqueue(new MockResponse()
                .setBody(emailResponse)
                .addHeader("Content-Type", "application/json")
        );

        Mono<String> email = emailNotificationService.sendNotification(NotificationDTO.builder()
                .fromAddress("someEmail")
                .toAddress("someEmail")
                .subject("subject")
                .attachmentLocation("someFile")
                .type("email")
                .build());
        Assertions.assertEquals(emailResponse,email.block());
    }
}