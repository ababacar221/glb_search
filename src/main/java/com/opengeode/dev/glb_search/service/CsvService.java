package com.opengeode.dev.glb_search.service;

import com.opencsv.bean.CsvToBean;
import com.opengeode.dev.glb_search.model.execution_flow.ExecutionFlow;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.List;

public interface CsvService {
    void readConfig(String destination, Collection<File> files) throws IOException;
}
