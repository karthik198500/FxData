package com.fxdata.fxeventsmonitor.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "webclient")
@Validated
@Setter
public class WebClientConfig {

    @NotBlank
    private String connectTimeout;
    @NotBlank
    private String responseTimeout;
    @NotBlank
    private String readTimeout;
    @NotBlank
    private String writeTimeout;


    public Integer getConnectTimeout() {
        return convertToInteger(connectTimeout);
    }

    public Integer getResponseTimeout() {
        return convertToInteger(responseTimeout);
    }

    public Integer getReadTimeout() {
        return convertToInteger(readTimeout);
    }

    public Integer getWriteTimeout() {
        return convertToInteger(writeTimeout);
    }

    public static Integer convertToInteger(String str){
        try{
            return Integer.parseInt(str);
        }catch (NumberFormatException e){
            throw new RuntimeException("Please configure Integer values for the ehs properties");
        }
    }
}
