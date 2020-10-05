package com.opengeode.dev.glb_search.controller;
import com.opengeode.dev.glb_search.model.Customer;
import com.opengeode.dev.glb_search.model.execution_flow.ExecutionFlow;
import com.opengeode.dev.glb_search.service.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    private ElasticsearchService elasticsearchService;


    @PostMapping("/ingest")
    public HttpStatus ingest_data() throws IOException,InterruptedException{
        return elasticsearchService.ingest_data("Employees50K.json");
    }

    @GetMapping("/")
    public List<Customer> get_all_data() throws IOException{
        return elasticsearchService.get_all_data();
    }
}
