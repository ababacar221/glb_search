package com.opengeode.dev.glb_search.controller;
import com.opengeode.dev.glb_search.model.CustomerLog;
import com.opengeode.dev.glb_search.dao.ElasticsearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private ElasticsearchRepository elasticsearchRepository;


    @PostMapping("/ingest")
    public HttpStatus ingest_data() throws IOException,InterruptedException{
        return elasticsearchRepository.ingest_data("Employees50K.json");
    }

    @GetMapping("/")
    public List<CustomerLog> get_all_data() throws IOException{
        return elasticsearchRepository.get_all_data();
    }
}
