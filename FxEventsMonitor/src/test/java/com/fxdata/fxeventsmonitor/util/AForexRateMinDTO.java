package com.fxdata.fxeventsmonitor.util;

import com.fxdata.fxeventsmonitor.dto.ForexRateMinDTO;

public class AForexRateMinDTO {

    private ForexRateMinDTO forexRateMinDTO = new ForexRateMinDTO();

    public ForexRateMinDTO defaults(){
         forexRateMinDTO.setCode(Some.code());
         forexRateMinDTO.setClose(Some.longVal(0,3));
         forexRateMinDTO.setTimestamp(Some.timeStamp());
        return forexRateMinDTO;
    }

    public static ForexRateMinDTO withDefaults() {
        return with()
                .defaults();
    }


    public static AForexRateMinDTO with() {
        return new AForexRateMinDTO();
    }

}
