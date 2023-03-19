package com.fxdata.fxeventsmonitor.writer;

import com.fxrates.fxeventsmonitor.dto.FxRateDTO;
import com.opencsv.CSVWriter;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.HeaderColumnNameMappingStrategyBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Log4j2
public class FxCSVWriter implements FxRateWriter<FxRateDTO> {

    private static final String FX_RATE_CSV_FILE_NAME_FORMAT = "YYYYMMdd_HHmm";
    private static DateFormat fxRateFormat = new SimpleDateFormat(FX_RATE_CSV_FILE_NAME_FORMAT);
    private static final String CSV = ".csv";

    @Override
    public void write(Collection<FxRateDTO> fxRateDTOList) {
        File file = new File(getFileName());
        //FxRateWriter writer = new FileWriter(resourceLoader.getResource("classpath:"+fxRate).getFile())
        try(Writer writer = new FileWriter(file);
            CSVWriter fxCsvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, '"', "\n")){
            HeaderColumnNameMappingStrategy<FxRateDTO> strategy = new HeaderColumnNameMappingStrategyBuilder<FxRateDTO>()
                    .build();
            strategy.setType(FxRateDTO.class);
            //strategy.setColumnOrderOnWrite(new OrderedComparatorIgnoringCase(TripInfo.FIELDS_ORDER));
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(fxCsvWriter)
                    .withMappingStrategy(strategy)
                    .build();
            beanToCsv.write(fxRateDTOList);
            fxRateDTOList.forEach(fxRateDTO -> {
                log.info(fxRateDTO.toString());
            });
        }catch (Exception e){
            log.error("Exception while writing",e);
            throw new RuntimeException(e);
        }
    }

    public String getFileName(){
        return fxRateFormat.format(new Date())+CSV;
    }
}
