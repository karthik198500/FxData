package com.fxdata.fxratesmonitor.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ForexRateMinDTO {

    private String code;
    private String timestamp;
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
