package com.opengeode.dev.glb_search.service.impl;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opengeode.dev.glb_search.helper.opencsv.CsvHelper;
import com.opengeode.dev.glb_search.helper.opencsv.HeaderCsv;
import com.opengeode.dev.glb_search.model.execution_flow.ExecutionFlow;
import com.opengeode.dev.glb_search.service.CsvService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CsvServiceImpl implements CsvService {

    @Autowired
    private CsvHelper csvHelper;

    @Override
    public void writeCSV(Object o, String f) throws IOException {
        //"message_log.csv"
        Writer writer = Files.newBufferedWriter(csvHelper.createDirectoryAndFile("src/test/csv_files",f).toPath());
        CSVPrinter printer = CSVFormat.DEFAULT.withHeader(HeaderCsv.class)
                .withQuote(CSVWriter.NO_QUOTE_CHARACTER)
                .print(writer);
        printer.printRecords(o);
        printer.flush();
        writer.close();
    }

    @Override
    public void writeCSV(List<Object> o,String file) throws IOException {
        //"message_log.csv"
        Writer writer = Files.newBufferedWriter(csvHelper.createDirectoryAndFile("src/test/csv_files",file).toPath());
        CSVPrinter printer = CSVFormat.DEFAULT.withHeader(HeaderCsv.class)
                .withQuote(CSVWriter.NO_QUOTE_CHARACTER)
                .print(writer);
        printer.printRecords(o);
        printer.flush();
        writer.close();
    }

    @Override
    public void readCSV(Object o,String directory,String file) throws IOException {
        Path path = csvHelper.createDirectoryAndFile(directory,file).toPath();
        Reader reader = Files.newBufferedReader(path);
        CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                .withType(o.getClass())
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        List<Object> objectList = new ArrayList();
        for (Object record: (Iterable<Object>) csvToBean){
            objectList.add(record);
        }
        reader.close();

    }
    @Override
    public CsvToBean readCSV(Reader reader, Object o) throws IOException {
        CsvToBean<ExecutionFlow> csvToBean = new CsvToBeanBuilder(reader)
                .withType(o.getClass())
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        return csvToBean;

    }
}
