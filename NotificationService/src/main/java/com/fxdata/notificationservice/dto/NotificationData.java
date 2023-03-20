package com.fxdata.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Validated
public class NotificationData {

    @JsonProperty("fromAddress")
    @NotBlank
    private String fromAddress;
    @JsonProperty("toAddress")
    @NotBlank
    private String toAddress;
    @JsonProperty("type")
    @NotBlank
    private String type;
    @JsonProperty("subject")
    private String subject;
    @JsonProperty("body")
    private String body;
    @JsonProperty("attachmentLocation")
    private String attachmentLocation;

    @Override
    public String toString() {
        return "NotificationData{" +
                "fromAddress='" + fromAddress + '\'' +
                ", toAddress='" + toAddress + '\'' +
                ", type='" + type + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", attachmentLocation='" + attachmentLocation + '\'' +
                '}';
    }
}
