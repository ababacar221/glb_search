package com.opengeode.dev.glb_search.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Context {
    private Long id;
    private String variable;
    private String description;
    private String default_value;
}
