package com.opengeode.dev.glb_search.helper.jms;

import com.opengeode.dev.glb_search.model.CustomerLog;
import com.opengeode.dev.glb_search.model.MessageStorage;
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
    private MessageStorage customerStorage;

    @Autowired
    private ElasticsearchRepository elasticsearchRepository;

    @JmsListener(destination = "${glb.activemq.queue}", containerFactory="jsaFactory")
    public void receive(CustomerLog customerLog) throws IOException, InterruptedException {
        System.out.println("Recieved Message: " + customerLog);
        log.info("Recieved Message: " + customerLog);
        customerStorage.add(customerLog);
        elasticsearchRepository.ingest_data(customerLog,index);
    }


}
