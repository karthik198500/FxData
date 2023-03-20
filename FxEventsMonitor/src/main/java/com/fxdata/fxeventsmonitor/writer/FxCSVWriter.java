package com.fxdata.fxeventsmonitor.writer;

import com.fxdata.fxeventsmonitor.config.FxEventsConfig;
import com.fxdata.fxeventsmonitor.dto.FxRateDTO;
import com.opencsv.CSVWriter;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.HeaderColumnNameMappingStrategyBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

@Log4j2
@Component
public class FxCSVWriter implements FxRateWriter<FxRateDTO> {

    private static final String FX_RATE_CSV_FILE_NAME_FORMAT = "YYYYMMdd_HHmm";
    private static DateFormat fxRateFormat = new SimpleDateFormat(FX_RATE_CSV_FILE_NAME_FORMAT);
    private static final String CSV = ".csv";
    private final FxEventsConfig fxEventsConfig;

    public FxCSVWriter(FxEventsConfig fxEventsConfig) {
        this.fxEventsConfig = fxEventsConfig;
    }

    @Override
    public String write(Collection<FxRateDTO> fxRateDTOList) {
        String fileName = getFileName();
        File file = new File(fileName);
        try(Writer writer = new FileWriter(file);
            CSVWriter fxCsvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, '"', "\n")){
            HeaderColumnNameMappingStrategy<FxRateDTO> strategy = new HeaderColumnNameMappingStrategyBuilder<FxRateDTO>()
                    .build();
            strategy.setType(FxRateDTO.class);
            //strategy.setColumnOrderOnWrite(new OrderedComparatorIgnoringCase(TripInfo.FIELDS_ORDER));
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(fxCsvWriter)
                    .withMappingStrategy(strategy)
                    .build();
            fxRateDTOList.forEach(new Consumer<FxRateDTO>() {
                @Override
                public void accept(FxRateDTO fxRateDTO) {
                    try {
                        beanToCsv.write(fxRateDTO);
                    } catch (CsvDataTypeMismatchException e) {
                        throw new RuntimeException(e);
                    } catch (CsvRequiredFieldEmptyException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            fxRateDTOList.forEach(fxRateDTO -> {
                log.info(fxRateDTO.toString());
            });
            return fileName;
        }catch (Exception e){
            log.error("Exception while writing",e);
            throw new RuntimeException(e);
        }
    }

    public String getFileName(){
        return fxEventsConfig.getFileStorageLocation()+"/"+fxRateFormat.format(new Date())+CSV;
    }
}
