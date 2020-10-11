package com.opengeode.dev.glb_search.helper.jms;

import com.opengeode.dev.glb_search.model.CustomerLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.Message;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class JmsProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${glb.activemq.queue}")
    String queue;

    public void send(CustomerLog customerLog){
        jmsTemplate.convertAndSend(queue, customerLog);
    }

    public void sendQueue(CustomerLog order) {
        log.info("sending with convertAndSend() to " + queue + " <" + order + ">");
        jmsTemplate.convertAndSend(queue, order, m -> {

            log.info("setting standard JMS headers before sending");
            m.setJMSCorrelationID(UUID.randomUUID().toString());
            m.setJMSExpiration(1000);
            //m.setJMSMessageID("message-id");
            m.setJMSMessageID(String.format("message-id-%s",UUID.randomUUID().toString()));
            m.setJMSDestination(new ActiveMQQueue(queue));
            m.setJMSReplyTo(new ActiveMQQueue(queue));
            m.setJMSDeliveryMode(JmsProperties.DeliveryMode.NON_PERSISTENT.getValue());
            m.setJMSPriority(Message.DEFAULT_PRIORITY);
            m.setJMSTimestamp(System.nanoTime());
            m.setJMSType("type");

            log.info("setting custom JMS headers before sending");
            m.setStringProperty("jms-custom-header", "this is a custom jms property");
            m.setBooleanProperty("jms-custom-property", true);
            m.setDoubleProperty("jms-custom-property-price", 0.0);

            return m;
        });

    }

}
