package com.opengeode.dev.glb_search.model;

import java.util.ArrayList;
import java.util.List;

public class MessageStorage {
    private List<CustomerLog> customerLogs = new ArrayList<>();

    public void add(CustomerLog customerLog) {
        customerLogs.add(customerLog);
    }

    public void clear() {
        customerLogs.clear();
    }

    public List<CustomerLog> getAll(){
        return customerLogs;
    }
}
