package com.opengeode.dev.glb_search.service;

import com.opengeode.dev.glb_search.model.Customer;
import com.opengeode.dev.glb_search.model.execution_flow.ExecutionFlow;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ElasticsearchService {

    HttpStatus ingest_data(String pathname) throws IOException,InterruptedException;

    HttpStatus ingest_data(List<Customer> exf, String index) throws InterruptedException,IOException;

    HttpStatus ingest_data(Customer obj, String index) throws InterruptedException,IOException;

    List<Customer> get_all_data() throws IOException;

    void createIndexIfNotPresent(String indexName, File indexFile);

    void createIndex(String indexName, File indexFile) throws IOException;

    void readJsonSchemaConfig(Collection<File> files) throws IOException;
}
