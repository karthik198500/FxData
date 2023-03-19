package com.fxdata.fxeventsmonitor;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Log4j2
public class EventsMonitor {
    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        log.info("Received <" + message + ">");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
