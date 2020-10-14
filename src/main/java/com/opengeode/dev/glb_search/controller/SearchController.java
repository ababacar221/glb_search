package com.opengeode.dev.glb_search.controller;
import com.opengeode.dev.glb_jms.model.ErrorLog;
import com.opengeode.dev.glb_search.dao.ElasticsearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private ElasticsearchRepository elasticsearchRepository;

    @GetMapping("/")
    public List<ErrorLog> get_all_data() throws IOException{
        return elasticsearchRepository.get_all_data();
    }
}
