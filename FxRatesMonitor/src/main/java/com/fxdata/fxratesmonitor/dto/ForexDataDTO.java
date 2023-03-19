package com.fxdata.fxratesmonitor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForexDataDTO {

    private String code;
    private String timestamp;
    private String gmtoffset;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;
    private String previousClose;
    private String change;
    private String change_p;

    @Override
    public String toString() {
        return "ForexDataDTO{" +
                "code='" + code + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", gmtoffset='" + gmtoffset + '\'' +
                ", open='" + open + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", close='" + close + '\'' +
                ", volume='" + volume + '\'' +
                ", previousClose='" + previousClose + '\'' +
                ", change='" + change + '\'' +
                ", change_p='" + change_p + '\'' +
                '}';
    }
}
