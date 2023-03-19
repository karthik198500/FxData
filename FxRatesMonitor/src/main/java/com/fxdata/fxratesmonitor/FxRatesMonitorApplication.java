package com.fxdata.fxratesmonitor;

import com.fxdata.fxratesmonitor.config.EhsConfiguration;
import com.fxdata.fxratesmonitor.engine.FxRatesEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({EhsConfiguration.class})
public class FxRatesMonitorApplication {

    private final FxRatesEngine fxRatesEngine;

    public FxRatesMonitorApplication(FxRatesEngine fxRatesEngine) {
        this.fxRatesEngine = fxRatesEngine;
    }

    public static void main(String[] args) {
        SpringApplication.run(FxRatesMonitorApplication.class, args);
        System.out.println();
    }
}
