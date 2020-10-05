package com.opengeode.dev.glb_search.model;

import java.util.ArrayList;
import java.util.List;

public class MessageStorage {
    private List<Customer> customers = new ArrayList<>();

    public void add(Customer customer) {
        customers.add(customer);
    }

    public void clear() {
        customers.clear();
    }

    public List<Customer> getAll(){
        return customers;
    }
}
