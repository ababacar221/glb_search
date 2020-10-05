package com.opengeode.dev.glb_search.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import com.opengeode.dev.glb_search.helper.jms.JmsProducer;
import com.opengeode.dev.glb_search.helper.opencsv.FileHelper;
import com.opengeode.dev.glb_search.model.Customer;
import com.opengeode.dev.glb_search.service.CsvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class CsvServiceImpl implements CsvService {

    @Autowired
    private JmsProducer jmsProducer;

    private Customer customer;


    public CsvToBean getCsvToBean(Reader reader, Object o) throws IOException {
        CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                .withType(o.getClass())
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        return csvToBean;
    }

    public void readCSV(File f){
        Map<String,Object> adp = new HashMap<String, Object>();
        List<Map<String,Object>> ad = new ArrayList<>();
        try {
            Reader reader = Files.newBufferedReader(f.toPath());
            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withSkipLines(1)
                    .build();
            List<String[]> records = csvReader.readAll();
            for (String[] record : records) {
                customer = new Customer();
                adp.put("ID",record[0]);
                adp.put("TIMESTAMP",new Date().toString());
                adp.put("PROJECT",record[2]);
                adp.put("ENTITY",record[3]);
                adp.put("JOB_NAME",record[4]);
                adp.put("COMPONENT",record[5]);
                adp.put("TYPE",record[6]);
                adp.put("ERROR_CODE",record[7]);
                adp.put("ERROR_MESSAGE",record[8]);
                log.info("ADP "+adp);
                customer.setLog(adp);
                jmsProducer.send(customer);
                ad.add(adp);
            }
            csvReader.close();
            reader.close();
        } catch (IOException | CsvException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void readConfig(String destination,Collection<File> files) throws IOException {
        AtomicInteger i = new AtomicInteger(1);
        files.forEach( f->{
            try {
                readCSV(f);
                if (Files.deleteIfExists(f.toPath())){
                    log.info(String.format("FILE  %s %s is deleted!",i,f.getName()));
                }else {
                    log.info(String.format("Sorry, unable to delete the file : N°%s %s.",i,f.getName()));
                }
                i.getAndIncrement();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
