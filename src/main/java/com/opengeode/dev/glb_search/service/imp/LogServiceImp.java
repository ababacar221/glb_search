package com.opengeode.dev.glb_search.service.imp;

import com.opengeode.dev.glb_jms.model.ErrorLog;
import com.opengeode.dev.glb_search.dao.LogRepository;

import java.io.File;
import java.io.IOException;

public class LogServiceImp implements LogRepository {
    @Override
    public ErrorLog readLog(File file) throws IOException {
        return null;
    }

    @Override
    public void writeLog(File file) throws IOException {

    }
}
