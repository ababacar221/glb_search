package com.opengeode.dev.glb_search.helper.jms;

import com.opengeode.dev.glb_search.model.execution_flow.ExecutionFlow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class JmsHelper {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendTo(String destination, List<ExecutionFlow> executionFlow){
        jmsTemplate.convertAndSend(destination,executionFlow);
        log.info("<ExecutionFlows> ::: "+executionFlow);
    }


}
