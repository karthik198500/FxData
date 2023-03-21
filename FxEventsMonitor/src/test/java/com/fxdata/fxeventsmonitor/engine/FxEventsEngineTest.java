package com.fxdata.fxeventsmonitor.engine;

import com.fxdata.fxeventsmonitor.dto.ForexRateMinDTO;
import com.fxdata.fxeventsmonitor.dto.FxRateDTO;
import com.fxdata.fxeventsmonitor.dto.NotificationDTO;
import com.fxdata.fxeventsmonitor.notify.NotificationService;
import com.fxdata.fxeventsmonitor.util.AForexRateMinDTO;
import com.fxdata.fxeventsmonitor.writer.FxRateWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
//@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class FxEventsEngineTest {

    private static  final String SUCCESSFULLY_SENT_DATA = "Successfully sent data";

    @Autowired
    @SpyBean
    FxEventsEngine fxEventsEngine;

    @MockBean
    FxRateWriter fxRateWriter;

    @MockBean
    NotificationService notificationService;

    @Test
    void contextLoads() {
    }
    @BeforeEach
    void setUp() {

    }

    @Test
    void sendEmptyMessage() {
        Assertions.assertDoesNotThrow(()->fxEventsEngine.sendMessage(null));
    }

    @Test
    void sendMessage() {
        List<ForexRateMinDTO> forexRateMinDTOList = someForexRateMinDTOS();
        doReturn(Mono.just(SUCCESSFULLY_SENT_DATA)).when(fxEventsEngine).run(Mockito.anyList());
        Assertions.assertDoesNotThrow(()->fxEventsEngine.sendMessage(forexRateMinDTOList));
    }

    @Test
    void processMessageThrowsException() {
        List<ForexRateMinDTO> forexRateMinDTOList = someForexRateMinDTOS();
        doThrow(new RuntimeException()).when(fxEventsEngine).run(Mockito.anyList());
        fxEventsEngine.sendMessage(forexRateMinDTOList).whenComplete((o, throwable) -> Assertions.assertTrue(null != throwable));
    }

    @Test
    void sendMessageAndCheckProcessing() {
        List<ForexRateMinDTO> forexRateMinDTOList = someForexRateMinDTOS();
        doReturn("Some Random File Location").when(fxRateWriter).write(Mockito.anyList());
        doReturn(Mono.just(SUCCESSFULLY_SENT_DATA)).when(notificationService).sendNotification(Mockito.any(NotificationDTO.class));

        fxEventsEngine.sendMessage(forexRateMinDTOList).whenComplete((result, throwable) -> {
                    Assertions.assertNotNull(result);
                    Mono<String> actualResult = Optional.of(result)
                            .filter(val -> val instanceof Mono)
                            .map(val -> (Mono<String>) result)
                            .orElse(Mono.empty());
                    Assertions.assertEquals(SUCCESSFULLY_SENT_DATA, actualResult.block());
                }
        );
    }

    private static List<ForexRateMinDTO> someForexRateMinDTOS() {
        List<ForexRateMinDTO> forexRateMinDTOList = new ArrayList<>();
        forexRateMinDTOList.add(AForexRateMinDTO.withDefaults());
        forexRateMinDTOList.add(AForexRateMinDTO.withDefaults());
        forexRateMinDTOList.add(AForexRateMinDTO.withDefaults());
        return forexRateMinDTOList;
    }

}