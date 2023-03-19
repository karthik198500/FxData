package com.fxdata.fxeventsmonitor.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
//Used builder here because in case you want to add more values its easy to construct using builder pattern
//rather than constructors.
public class FxRateDTO {
    private String forex;
    private String value;

}
