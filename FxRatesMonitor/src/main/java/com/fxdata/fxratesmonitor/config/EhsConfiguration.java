package com.fxdata.fxratesmonitor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


import javax.validation.constraints.NotBlank;
import java.util.List;

@ConfigurationProperties(prefix = "ehs")
@Validated
@Setter
public class EhsConfiguration {


    @NotBlank
    @Getter
    private String url;
    @NotBlank
    @Getter
    private String apiToken;
    @NotBlank
    @Getter
    private String fmt;
    @Getter
    private List<String> forexTypes;
    @NotBlank
    private String connectTimeout;
    @NotBlank
    private String responseTimeout;
    @NotBlank
    private String readTimeout;
    @NotBlank
    private String writeTimeout;

    /*@Validated
    public static class ForexType {
        @NotNull(message = ValidationMessage.FOREX_TYPE_NOT_EMPTY)
        @NotBlank(message = ValidationMessage.FOREX_TYPE_NOT_EMPTY)
        private String forexType;
    }*/

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
