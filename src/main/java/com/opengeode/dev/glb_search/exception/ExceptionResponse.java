package com.opengeode.dev.glb_search.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    //timestamp
    private Date timestamp;
    //message
    private String message;
    //status
    //private int status;
    //details
    private String details;
}
