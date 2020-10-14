package com.opengeode.dev.glb_search.service.imp;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import com.opengeode.dev.glb_jms.model.ErrorLog;
import com.opengeode.dev.glb_search.helper.jms.JmsProducer;
import com.opengeode.dev.glb_search.dao.CsvRepository;
import com.opengeode.dev.glb_search.helper.opencsv.FileHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
public class CsvServiceImp implements CsvRepository {

    @Autowired
    private JmsProducer jmsProducer;

    private ErrorLog errorLog;

    @Value("${path.directory.archive}")
    private String path_archive;

    @Autowired
    private FileHelper fileHelper;

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
                    //.withSkipLines(1)
                    .build();
            List<String[]> records = csvReader.readAll();
            ArrayList<String> lineHeader = new ArrayList<>();
            boolean headersConsumed = false;
            for (String[] record : records) {
                if(!headersConsumed){
                    for (int i=0;i<record.length;i++){
                        log.info("RECORD : "+i +" : "+record[i]);
                        lineHeader.add(record[i]);
                    }
                    log.info("LINE HEADER == "+lineHeader);
                    headersConsumed = true;
                    continue;
                }
                errorLog = new ErrorLog();
                for (int i=0;i<record.length;i++){
                    adp.put(lineHeader.get(i),record[i]);
                }
                adp.put("timestamp",new Date());
                log.info("ADP "+adp);
                errorLog.setLog(adp);
                jmsProducer.sendQueue(errorLog);
                ad.add(adp);
            }
            csvReader.close();
            reader.close();
        } catch (IOException | CsvException ex) {
            ex.printStackTrace();
        }
    }

    public void writeArchive(File file) throws IOException {
        String[] f = file.toPath().getFileName().toString().split("\\.(?=[^\\.]+$)");
        log.info("FILENAMMMMM ++++++=="+ Arrays.toString(f));
        Files.move(file.toPath(),fileHelper.createDirectoryAndFile(path_archive,String.format("%s_%s.%s",f[0],new Date().toString(),f[1])).toPath(),REPLACE_EXISTING);
    }

    @Override
    public void readConfig(String destination,Collection<File> files) throws IOException {
        AtomicInteger i = new AtomicInteger(1);
        files.forEach( f->{
            try {
                readCSV(f);
                writeArchive(f);
//                if (Files.deleteIfExists(f.toPath())){
//                    log.info(String.format("FILE  %s %s is deleted!",i,f.getName()));
//                }else {
//                    log.info(String.format("Sorry, unable to delete the file : N°%s %s.",i,f.getName()));
//                }
                //Archive
                i.getAndIncrement();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
