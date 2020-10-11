package com.opengeode.dev.glb_search.task;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimerTask;

@Slf4j
public class LoadDataTask extends TimerTask {

    @SneakyThrows
    @Override
    public void run() {
        log.warn("Data Read at: "
                + LocalDateTime.ofInstant(Instant.ofEpochMilli(scheduledExecutionTime()),
                ZoneId.systemDefault()));
    }
}
