package com.opengeode.dev.glb_search.controller;

import com.opengeode.dev.glb_search.helper.jms.JmsProducer;
import com.opengeode.dev.glb_search.model.CustomerLog;
import com.opengeode.dev.glb_search.model.MessageStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/error")
public class RestAPIs {
    @Autowired
    JmsProducer jmsProducer;

    @Autowired
    private MessageStorage customerStorage;

    @PostMapping(value="/customer")
    public CustomerLog postCustomer(@RequestBody CustomerLog customerLog){
        jmsProducer.send(customerLog);
        return customerLog;
    }

    @GetMapping(value="/customers")
    public List<CustomerLog> getAll(){
        List<CustomerLog> customerLogs = customerStorage.getAll();
        return customerLogs;
    }

    @DeleteMapping(value="/customers/clear")
    public String clearCustomerStorage() {
        customerStorage.clear();
        return "Clear All CustomerStorage!";
    }
}
