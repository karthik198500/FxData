package com.fxdata.fxeventsmonitor.writer;

import com.fxdata.fxeventsmonitor.dto.FxRateDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class CSVWriterTest {

    //@Test
    void getFileName(){
        FxCSVWriter fxCsvWriter = new FxCSVWriter();
        String fileName = fxCsvWriter.getFileName();
    }

    //@Test
    void writeFxRateDTOToCSV(){

        ArrayList<FxRateDTO> fxRateDTOArrayList = new ArrayList<>();
        fxRateDTOArrayList.add(FxRateDTO.builder()
                .forex("AUDUS")
                .value("0.65")
                .build());
        FxCSVWriter fxCsvWriter = new FxCSVWriter();
        fxCsvWriter.write(fxRateDTOArrayList);

    }
}