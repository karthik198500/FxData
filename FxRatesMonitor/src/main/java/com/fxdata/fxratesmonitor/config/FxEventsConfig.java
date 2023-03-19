package com.fxdata.fxratesmonitor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "fxevent")
@Validated
@Setter
@Getter
public class FxEventsConfig {

    @NotBlank
    private String fileStorageLocation;
    private Notification notification;

    @Validated
    @Getter
    @Setter
    @NotNull
    public static class Notification{
        private String serviceUrl;
        @NotBlank
        private String fromAddress;
        @NotBlank
        private String toAddress;
        private String subject;
        @NotBlank
        private String type;
    }

}
