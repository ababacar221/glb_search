package com.opengeode.dev.glb_search.service.imp;

import com.opengeode.dev.glb_search.helper.excel.ExcelHelper;
import com.opengeode.dev.glb_search.model.Context;
import com.opengeode.dev.glb_search.dao.ElasticsearchRepository;
import com.opengeode.dev.glb_search.dao.ExcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelServiceImp implements ExcelRepository {

    @Autowired
    private ElasticsearchRepository elasticsearchRepository;

    @Value("${elasticsearch.destination.config}")
    private String index_config;

    @Override
    public void save(MultipartFile file) {
        try {
            List<Context> contexts = ExcelHelper.excelToContexts(file.getInputStream());
            elasticsearchRepository.save(contexts,index_config);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    @Override
    public ByteArrayInputStream load() {

        try {
            List<Context> contexts = elasticsearchRepository.findAll(index_config);
            ByteArrayInputStream in = ExcelHelper.contextToExcel(contexts);
            return in;
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }

    }

    @Override
    public List<Context> getAllContexts() {
        try {
            return elasticsearchRepository.findAll(index_config);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }
}
