package com.fxdata.fxeventsmonitor.notify;

import com.fxdata.fxeventsmonitor.FxEventsMonitorApplication;
import com.fxdata.fxeventsmonitor.config.FxEventsConfig;
import com.fxdata.fxeventsmonitor.dto.NotificationDTO;
import com.fxdata.fxeventsmonitor.util.Some;
import okhttp3.MediaType;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = {"classpath:application-test.properties"})
class EmailNotificationServiceTest {

    @Autowired
    @InjectMocks
    EmailNotificationService emailNotificationService;

    @MockBean
    FxEventsConfig fxEventsConfig;

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
                .fromAddress(Some.email())
                .toAddress(Some.email())
                .subject(Some.subject())
                .attachmentLocation(Some.someFile().getAbsolutePath())
                .type("email")
                .build());
        Assertions.assertEquals(emailResponse,email.block());
    }
}