package com.opengeode.dev.glb_search.config;

import com.opengeode.dev.glb_search.dao.CsvRepository;
import com.opengeode.dev.glb_search.dao.ElasticsearchRepository;
import com.opengeode.dev.glb_search.helper.opencsv.FileHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@Slf4j
public class BeanConfiguration {

    @Autowired
    private ElasticsearchRepository elasticsearchRepository;

    @Autowired
    private FileHelper fileHelper;

    @Autowired
    private CsvRepository csvRepository;

    @Value("${glb.activemq.queue}")
    private String destination;

    @Value("${path.directory.config}")
    private String directory_data_config;

    @Value("${path.directory.data}")
    private String directory_data;


    @Bean
    public void readSchema() throws IOException {
        log.info("READING SCHEMA ...");
        elasticsearchRepository.readJsonSchemaConfig(fileHelper.readerFileConfig(directory_data_config));
        csvRepository.readConfig(destination, fileHelper.readerFileConfig(directory_data));
    }

    @Bean
    public void executeBeanFile() {
        new Thread(new Runnable() {
            @SneakyThrows
            public void run() {
                while (true) {
                    csvRepository.readConfig(destination, fileHelper.readerFileConfig(directory_data));
                }
            }
        }).start();
    }

}
