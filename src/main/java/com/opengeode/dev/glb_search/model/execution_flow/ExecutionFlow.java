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
@ToString
public class ExecutionFlow {
    //File structure:
    //timestamp;project;entity;jobname;component;type;error code;error message
    @CsvBindByName
    @JsonProperty("id")
    private Integer id;
    @CsvBindByName
    @JsonProperty("Timestamp")
    private String timestamp;
    @CsvBindByName
    @JsonProperty("Project")
    private String project;
    @CsvBindByName
    @JsonProperty("Entity")
    private String entity;
    @CsvBindByName
    @JsonProperty("JobName")
    private String jobName;
    @CsvBindByName
    @JsonProperty("Component")
    private String component;
    @CsvBindByName
    @JsonProperty("Type")
    private String type;
    @CsvBindByName
    @JsonProperty("ErrorCode")
    private String error_code;
    @CsvBindByName
    @JsonProperty("ErrorMessage")
    private String error_message;

    @Override
    public String toString(){
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",getId(),getTimestamp(),getProject(),getEntity(),getJobName(),getComponent(),getType(),getError_code(),getError_message());
    }

}
