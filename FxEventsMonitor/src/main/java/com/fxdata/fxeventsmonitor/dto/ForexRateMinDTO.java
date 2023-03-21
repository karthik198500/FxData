package com.fxdata.fxeventsmonitor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class ForexRateMinDTO implements Serializable {

    @JsonProperty("code")
    private String code;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("close")
    private String close;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForexRateMinDTO)) return false;
        ForexRateMinDTO that = (ForexRateMinDTO) o;
        return code.equals(that.code) && close.equals(that.close);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, close);
    }

    @Override
    public String toString() {
        return "ForexRateMinDTO{" +
                "code='" + code + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", close='" + close + '\'' +
                '}';
    }
}
