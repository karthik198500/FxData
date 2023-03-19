package com.fxdata.fxeventsmonitor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class NotificationDTO {
    private String fromAddress;
    private String toAddress;
    private String type;
    private String subject;
    private String body;
    private String attachmentLocation;
}
