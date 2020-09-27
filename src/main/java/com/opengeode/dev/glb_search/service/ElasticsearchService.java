package com.opengeode.dev.glb_search.service;

import com.opengeode.dev.glb_search.model.execution_flow.ExecutionFlow;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

public interface ElasticsearchService {

    HttpStatus ingest_data(String pathname) throws IOException,InterruptedException;

    HttpStatus ingest_data(List<ExecutionFlow> executionFlows, String index) throws InterruptedException,IOException;

    List<ExecutionFlow> get_all_data() throws IOException;

    List<ExecutionFlow> get_specific_docs(String search_string) throws IOException;
}
