package com.fxdata.fxratesmonitor.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fxdata.fxratesmonitor.config.EhsConfiguration;
import com.fxdata.fxratesmonitor.httpclient.EhsWebClientBuilder;
import com.fxdata.fxratesmonitor.notify.NotificationService;
import com.fxdata.fxratesmonitor.util.AForexRateMinDTO;
import com.fxdata.fxratesmonitor.util.ExceptionHandler;
import com.fxdata.fxratesmonitor.util.Some;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class FxRatesEngineTest {

    public static MockWebServer mockWebServer;

    @InjectMocks
    @SpyBean
    private FxRatesEngine fxRatesEngine;

    @MockBean
    private EhsConfiguration ehsConfiguration;

    @MockBean
    private TopicSender topicSender;

    @MockBean
    private EhsWebClientBuilder ehsWebClientBuilder;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private ExceptionHandler exceptionHandler;

    @Test
    void contextLoads() {
    }

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void initialize() {

        /*String baseUrl = String.format("http://localhost:%s",
                mockWebServer.getPort());*/

        ehsConfiguration = mock(EhsConfiguration.class);
        topicSender = mock(TopicSender.class);
        notificationService = mock(NotificationService.class);
        exceptionHandler = mock(ExceptionHandler.class);



        when(ehsConfiguration.getUrl()).thenReturn(mockWebServer.url("/api/real-time/").toString());
        when(ehsConfiguration.getApiToken()).thenReturn(Some.longVal(10000,1000000));
        when(ehsConfiguration.getFmt()).thenReturn("json");
        when(ehsConfiguration.getForexTypes()).thenReturn(Arrays.asList("AUDUSD","AUDNZ","AUDHKG"));

        when(ehsConfiguration.getConnectTimeout()).thenReturn(60000);
        when(ehsConfiguration.getReadTimeout()).thenReturn(60000);
        when(ehsConfiguration.getWriteTimeout()).thenReturn(60000);
        doNothing().when(exceptionHandler).handleException(Mockito.any(Throwable.class));



        ehsWebClientBuilder = spy(new EhsWebClientBuilder(ehsConfiguration));

        fxRatesEngine = spy(new FxRatesEngine(ehsWebClientBuilder,ehsConfiguration,notificationService,topicSender, exceptionHandler));
    }

    @Test
    void fetchAndProcessForexDataSuccessCriteria() {
        doNothing().when(topicSender).send(Mockito.anyList());
        mockWebServer.enqueue(new MockResponse()
                .setBody(getForexRateMinDTOJson())
                .addHeader("Content-Type", "application/json")
        );
        mockWebServer.enqueue(new MockResponse()
                .setBody(getForexRateMinDTOJson())
                .addHeader("Content-Type", "application/json")
        );
        mockWebServer.enqueue(new MockResponse()
                .setBody(getForexRateMinDTOJson())
                .addHeader("Content-Type", "application/json")
        );
        Mono<List<Object>> listMono = fxRatesEngine.fetchAndProcessForexData();

    }

    @Test
    void fetchAndProcessForexDataFailureScenario() {
        doThrow(new RuntimeException()).when(topicSender).send(Mockito.anyList());
        mockWebServer.enqueue(new MockResponse()
                .setBody(getForexRateMinDTOJson())
                .setHeader("Content-Type", "application/json"));
        mockWebServer.enqueue(new MockResponse()
                        .setResponseCode(500)
                        .setBody("{}")
                .setHeader("Content-Type", "application/json"));
        mockWebServer.enqueue(new MockResponse()
                .setBody(getForexRateMinDTOJson())
                .setHeader("Content-Type", "application/json"));

        Mono<List<Object>> listMono = fxRatesEngine.fetchAndProcessForexData();
        StepVerifier.create(listMono).verifyError();
    }

    private static String getForexRateMinDTOJson() {
        String forexRateMinDTOJson = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            forexRateMinDTOJson = objectMapper.writeValueAsString(AForexRateMinDTO.withDefaults());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return forexRateMinDTOJson;
    }
}