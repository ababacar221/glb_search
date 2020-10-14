package com.opengeode.dev.glb_search.dao;

import com.opengeode.dev.glb_jms.model.ErrorLog;

import java.io.File;
import java.io.IOException;

public interface LogRepository {

    ErrorLog readLog(File file) throws IOException;

    void writeLog(File file) throws IOException;
}
