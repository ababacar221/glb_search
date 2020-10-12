package com.opengeode.dev.glb_search.helper.jms;

import com.opengeode.dev.glb_search.model.ErrorLog;
import com.opengeode.dev.glb_search.dao.ElasticsearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class JmsConsumer {

    @Value("${elasticsearch.destination}")
    private String index;

    @Autowired
    private ElasticsearchRepository elasticsearchRepository;

    @JmsListener(destination = "${glb.activemq.queue}", containerFactory="jsaFactory")
    public void receive(ErrorLog errorLog) throws IOException, InterruptedException {
        System.out.println("Received Message: " + errorLog);
        log.info("Received Message: " + errorLog);
        elasticsearchRepository.ingest_data(errorLog,index);
    }


}
