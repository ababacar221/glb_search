package com.opengeode.dev.glb_search.model.execution_flow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExecutionFlow {
    private Integer id;
    private String timestamp;
    private String project;
    private String entity;
    private String jobName;
    private String component;
    private String type;
    private String error_code;
    private String error_message;

    @Override
    public String toString(){
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",getId(),getTimestamp(),getProject(),getEntity(),getJobName(),getComponent(),getType(),getError_code(),getError_message());
    }

}
