package com.opengeode.dev.glb_search.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private String id = UUID.randomUUID().toString();
    private Map<String, Object> log;
}
