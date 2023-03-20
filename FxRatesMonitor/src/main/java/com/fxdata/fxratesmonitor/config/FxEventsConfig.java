package com.fxdata.fxratesmonitor.config;

import com.fxdata.fxratesmonitor.util.ValidationMessage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "fxevents")
@Validated
@Setter
@Getter
public class FxEventsConfig {

    @NotNull
    private Notification notification;

    @Validated
    @Getter
    @Setter
    @NotNull
    public static class Notification{

        @NotBlank
        private String serviceUrl;
        @NotBlank
        private String fromAddress;
        @NotBlank
        private String toAddress;
        private String subject;
        @NotBlank(message = ValidationMessage.FOREX_TYPE_NOT_EMPTY)
        private String type;
    }

}
