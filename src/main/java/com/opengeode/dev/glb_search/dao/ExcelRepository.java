package com.opengeode.dev.glb_search.dao;

import com.opengeode.dev.glb_search.model.Context;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface ExcelRepository {

    void save(MultipartFile file);

    ByteArrayInputStream load();

    List<Context> getAllContexts();
}
