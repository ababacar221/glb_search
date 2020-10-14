package com.opengeode.dev.glb_search.controller;

import com.opengeode.dev.glb_jms.model.ErrorLog;
import com.opengeode.dev.glb_search.helper.jms.JmsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/error")
public class RestAPIs {

    @Autowired
    JmsProducer jmsProducer;

    @PostMapping(value="/log")
    public ErrorLog postCustomer(@RequestBody ErrorLog errorLog){
        jmsProducer.sendQueue(errorLog);
        return errorLog;
    }
}
