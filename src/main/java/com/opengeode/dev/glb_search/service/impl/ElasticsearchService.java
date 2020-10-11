package com.opengeode.dev.glb_search.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opengeode.dev.glb_search.model.Context;
import com.opengeode.dev.glb_search.model.CustomerLog;
import com.opengeode.dev.glb_search.dao.ElasticsearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
@Slf4j
public class ElasticsearchService implements ElasticsearchRepository {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${elasticsearch.destination}")
    private String elasticsearch_index;

    @Override
    public HttpStatus ingest_data(String pathname) throws IOException, InterruptedException {
        File jsonFile = new File(pathname);
        FileReader fr = new FileReader(jsonFile);
        BufferedReader br = new BufferedReader(fr);
        String line;
        List<CustomerLog> customerLogs = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        while ((line = br.readLine()) != null) {
            CustomerLog customerLog = mapper.readValue(line, CustomerLog.class);
            customerLogs.add(customerLog);
        }
        ingest_data(customerLogs,null);
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus ingest_data(List<CustomerLog> exf, String index) throws InterruptedException, IOException {
        BulkProcessor bulkProcessor = bulk_Processor();
        try {
            set_search(index, bulkProcessor, exf);
        } catch (Exception e) {
            log.error("Error encountered", e);
            throw e;
        }
        return getHttpStatus(bulkProcessor);
    }

    @Override
    public HttpStatus save(List<Context> contexts, String index) throws InterruptedException, IOException {
        BulkProcessor bulkProcessor = bulk_Processor();
        try {
            this.get_search(index, bulkProcessor, contexts);
        } catch (Exception e) {
            log.error("Error encountered", e);
            throw e;
        }
        return getHttpStatus(bulkProcessor);
    }

    private void get_search(String index, BulkProcessor bulkProcessor, CustomerLog emp) {
        Map<String, Object> map = objectMapper.convertValue(emp.getLog(), HashMap.class);
        map.values().removeAll(Collections.singleton(null));
        Set<String> keys1 = map.keySet();
        XContentBuilder builder = null;
        try {
            builder = jsonBuilder().startObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String keys : keys1) {
            try {
                builder.field(keys, map.get(keys));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            builder.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = index != null ? index : "default";
        String index_search = String.format("%s", s.toLowerCase());
        IndexRequest indexRequest = new IndexRequest(index_search, "_doc", emp.getId().toString())
                .source(builder)
                .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        UpdateRequest updateRequest = new UpdateRequest(index_search, "_doc", emp.getId().toString());
        updateRequest.doc(builder);
        updateRequest.upsert(indexRequest);

        bulkProcessor.add(updateRequest);
    }

    private void set_search(String index, BulkProcessor bulkProcessor, List<CustomerLog> customerLogs) {
        customerLogs.forEach(emp -> {
            Map<String, Object> map = objectMapper.convertValue(emp.getLog(), HashMap.class);
            map.values().removeAll(Collections.singleton(null));
            Set<String> keys1 = map.keySet();
            XContentBuilder builder = null;
            try {
                builder = jsonBuilder().startObject();
            } catch (IOException e) {
                e.printStackTrace();
                ;
            }
            for (String keys : keys1) {
                try {
                    builder.field(keys, map.get(keys));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                builder.endObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String s = index != null ? index : "default";
            String index_search = String.format("%s", s.toLowerCase());
            IndexRequest indexRequest = new IndexRequest(index_search, "_doc", emp.getId().toString())
                    .source(builder)
                    .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            UpdateRequest updateRequest = new UpdateRequest(index_search, "_doc", emp.getId().toString());
            updateRequest.doc(builder);
            updateRequest.upsert(indexRequest);
            bulkProcessor.add(updateRequest);
        });
    }

    private void get_search(String index, BulkProcessor bulkProcessor, List<Context> contexts) {
        contexts.forEach(ctx -> {
            Map<String, Object> map = objectMapper.convertValue(ctx, HashMap.class);
            map.values().removeAll(Collections.singleton(null));
            Set<String> keys1 = map.keySet();
            XContentBuilder builder = null;
            try {
                builder = jsonBuilder().startObject();
            } catch (IOException e) {
                e.printStackTrace();
                ;
            }
            for (String keys : keys1) {
                try {
                    builder.field(keys, map.get(keys));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                builder.endObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String s = index != null ? index : "default";
            String index_search = String.format("%s", s.toLowerCase());
            IndexRequest indexRequest = new IndexRequest(index_search, "_doc", ctx.getId().toString())
                    .source(builder)
                    .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            UpdateRequest updateRequest = new UpdateRequest(index_search, "_doc", ctx.getId().toString());
            updateRequest.doc(builder);
            updateRequest.upsert(indexRequest);
            bulkProcessor.add(updateRequest);
        });
    }

    @Override
    public HttpStatus ingest_data(CustomerLog obj, String index) throws InterruptedException, IOException {
        BulkProcessor bulkProcessor = bulk_Processor();
        try {
            get_search(index, bulkProcessor, obj);
        } catch (Exception e) {
            log.error("Error encountered", e);
            throw e;
        }
        return getHttpStatus(bulkProcessor);
    }

    private HttpStatus getHttpStatus(BulkProcessor bulkProcessor) throws InterruptedException {
        try {
            boolean terminated = bulkProcessor.awaitClose(30L, TimeUnit.SECONDS);
            log.info("Record Updation Success {}", terminated);
            System.out.println("Updated");
        } catch (InterruptedException e) {
            log.error("Error", e);
            throw e;
        }

        return HttpStatus.OK;
    }

    private BulkProcessor bulk_Processor() {
        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long l, BulkRequest bulkRequest) {
                log.info("Updating Started");
            }
            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                if (bulkResponse.hasFailures()) {
                    for (BulkItemResponse bulkItemResponse : bulkResponse) {
                        if (bulkItemResponse.isFailed()) {
                            BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                            log.info("Error ", failure.getCause());
                        }
                    }
                }
            }
            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
                log.error("Error encountered", throwable);
            }
        };

        BulkProcessor build = BulkProcessor.builder(
                (request, bulkListener) ->
                        restHighLevelClient.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
                listener).build();
        return build;
    }

    @Override
    public List<CustomerLog> get_all_data() throws IOException {
        return getCustomers(elasticsearch_index);

    }

    @Override
    public List<Context> findAll(String index) throws IOException {
        return getContexts(index);
    }

    private List<CustomerLog> getCustomers(String index) throws IOException {
        SearchResponse searchResponse = searchResponse(index);
        SearchHit[] searchHit = searchResponse.getHits().getHits();
        List<CustomerLog> customerLogs = new ArrayList<>();
        for (SearchHit hit : searchHit) {
            Map map = hit.getSourceAsMap();
            CustomerLog customerLog = objectMapper.convertValue(hit.getSourceAsMap(), CustomerLog.class);
            customerLog.setLog(map);
            customerLogs.add(customerLog);
        }
        return customerLogs;
    }

    private SearchResponse searchResponse(String index) throws IOException {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index.toLowerCase());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(25);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return searchResponse;
    }

    private List<Context> getContexts(String index) throws IOException {
        SearchResponse searchResponse = searchResponse(index);
        SearchHit[] searchHit = searchResponse.getHits().getHits();
        List<Context> contexts = new ArrayList<>();
        for (SearchHit hit : searchHit) {
            Context context = objectMapper.convertValue(hit.getSourceAsMap(), Context.class);
            contexts.add(context);
        }
        return contexts;
    }

    @Override
    public void createIndexIfNotPresent(String indexName, File indexFile) {
        try {
            GetIndexRequest request = new GetIndexRequest(indexName.toLowerCase());
            if (!restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT)) {
                createIndex(indexName, indexFile);
            }
        } catch (IOException e) {
            log.error(String.format("Can not create index %s", indexName), e);
        }
    }

    @Override
    public void createIndex(String indexName, File indexFile) throws IOException {
        String mapping =  new String(Files.readAllBytes(indexFile.toPath()));
        log.info("NEW MAPPING == "+mapping);
        CreateIndexRequest indexRequest = new CreateIndexRequest(indexName);
        indexRequest.mapping(mapping, XContentType.JSON);
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(indexRequest, RequestOptions.DEFAULT);
        if (!createIndexResponse.isAcknowledged()) {
            log.error(String.format("Can not create index %s", indexName));
        }
    }

    @Override
    public void readJsonSchemaConfig(Collection<File> files) throws IOException {
        files.forEach(f->{
            if(f.toPath().toString().endsWith(".json")){
                createIndexIfNotPresent(elasticsearch_index.toLowerCase(),f);
            }
        });
    }
}
