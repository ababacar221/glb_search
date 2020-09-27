package com.opengeode.dev.glb_search.service;

import com.opencsv.bean.CsvToBean;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public interface CsvService {

    void writeCSV(Object o,String file) throws IOException;

    public void writeCSV(List<Object> o,String file) throws IOException;

    void readCSV(Object o,String directory,String file) throws IOException;

    CsvToBean readCSV(Reader reader, Object o) throws IOException;
}
