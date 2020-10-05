package com.opengeode.dev.glb_search.helper.jms;

import com.opengeode.dev.glb_search.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JmsProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${glb.activemq.queue}")
    String queue;

    public void send(Customer customer){
        jmsTemplate.convertAndSend(queue, customer);
    }

}
