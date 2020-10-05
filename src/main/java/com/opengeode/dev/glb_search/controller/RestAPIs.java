package com.opengeode.dev.glb_search.controller;

import com.opengeode.dev.glb_search.helper.jms.JmsProducer;
import com.opengeode.dev.glb_search.model.Customer;
import com.opengeode.dev.glb_search.model.MessageStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestAPIs {
    @Autowired
    JmsProducer jmsProducer;

    @Autowired
    private MessageStorage customerStorage;

    @PostMapping(value="/api/customer")
    public Customer postCustomer(@RequestBody Customer customer){
        jmsProducer.send(customer);
        return customer;
    }

    @GetMapping(value="/api/customers")
    public List<Customer> getAll(){
        List<Customer> customers = customerStorage.getAll();
        return customers;
    }

    @DeleteMapping(value="/api/customers/clear")
    public String clearCustomerStorage() {
        customerStorage.clear();
        return "Clear All CustomerStorage!";
    }
}
