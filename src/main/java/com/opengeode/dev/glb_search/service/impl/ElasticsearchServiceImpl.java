package com.opengeode.dev.glb_search.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opengeode.dev.glb_search.model.execution_flow.ExecutionFlow;
import com.opengeode.dev.glb_search.service.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
@Slf4j
public class ElasticsearchServiceImpl implements ElasticsearchService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public HttpStatus ingest_data(String pathname) throws IOException, InterruptedException {
        // Here Enter path to your Json File.
        //"Employees50K.json"
        File jsonFile = new File(pathname);
        // reads the file
        FileReader fr = new FileReader(jsonFile);
        BufferedReader br = new BufferedReader(fr);
        String line;
        List<ExecutionFlow> executionFlowList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        Integer count = 1;
        while ((line = br.readLine()) != null) {
            ExecutionFlow executionFlow = mapper.readValue(line, ExecutionFlow.class);
            executionFlow.setId(count);
            executionFlowList.add(executionFlow);
            count++;
        }
        ingest_data(executionFlowList,null);
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus ingest_data(List<ExecutionFlow> executionFlows, String index) throws InterruptedException, IOException {
        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long l, BulkRequest bulkRequest) {
                log.info("Updating Started");
            }

            // error is logged here
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

        BulkProcessor bulkProcessor = BulkProcessor.builder(
                (request, bulkListener) ->
                        restHighLevelClient.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
                listener).build();

        try {
            BulkRequest bulkRequest = new BulkRequest();

            executionFlows.forEach(emp -> {
                Map<String, Object> map = objectMapper.convertValue(emp, HashMap.class);
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
                String index_search = String.format("%s_index",s.toLowerCase());
                IndexRequest indexRequest = new IndexRequest(index_search, "_doc", emp.getId().toString())
                        .source(builder);
                UpdateRequest updateRequest = new UpdateRequest(index_search, "_doc", emp.getId().toString());
                updateRequest.doc(builder);
                updateRequest.upsert(indexRequest);

                bulkProcessor.add(updateRequest);
            });
        } catch (Exception e) {
            log.error("Error encountered", e);
            throw e;
        }

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

    @Override
    public List<ExecutionFlow> get_all_data() throws IOException {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("employee_index");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(25);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHit = searchResponse.getHits().getHits();
        List<ExecutionFlow> executionFlowList = new ArrayList<>();
        for (SearchHit hit : searchHit) {
            executionFlowList.add(objectMapper.convertValue(hit.getSourceAsMap(), ExecutionFlow.class));
        }
        return executionFlowList;
    }

    @Override
    public List<ExecutionFlow> get_specific_docs(String search_string) throws IOException {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.termQuery("Designation", search_string));
        boolQueryBuilder.should(QueryBuilders.termQuery("MaritalStatus", search_string));
        boolQueryBuilder.should(QueryBuilders.matchQuery("FirstName", search_string).boost(0.4f)
                .fuzziness(Fuzziness.AUTO));
        boolQueryBuilder.should(QueryBuilders.matchQuery("LastName", search_string).boost(0.3f)
                .fuzziness(Fuzziness.AUTO));
        boolQueryBuilder.should(QueryBuilders.matchQuery("Interests", search_string).boost(0.4f)
                .fuzziness(Fuzziness.AUTO));
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("employee_index");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        SearchHit[] searchHit = searchResponse.getHits().getHits();
        List<ExecutionFlow> executionFlowList = new ArrayList<>();
        for (SearchHit hit : searchHit){
            executionFlowList.add(objectMapper.convertValue(hit.getSourceAsMap(), ExecutionFlow.class));
        }

        return executionFlowList;
    }
}
