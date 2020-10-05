package com.opengeode.dev.glb_search.helper.jms;

import com.opengeode.dev.glb_search.model.Customer;
import com.opengeode.dev.glb_search.model.MessageStorage;
import com.opengeode.dev.glb_search.model.execution_flow.ExecutionFlow;
import com.opengeode.dev.glb_search.service.ElasticsearchService;
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
    private ElasticsearchService elasticsearchService;

    @JmsListener(destination = "${glb.activemq.queue}", containerFactory="jsaFactory")
    public void receive(Customer customer) throws IOException, InterruptedException {
        System.out.println("Recieved Message: " + customer);
        log.info("Recieved Message: " + customer);
        customerStorage.add(customer);
        elasticsearchService.ingest_data(customer,index);
    }


}
