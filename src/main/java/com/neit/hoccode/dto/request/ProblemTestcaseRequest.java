package com.neit.hoccode.dto.request;

import lombok.Data;

@Data
public class ProblemTestcaseRequest {
    private Integer id;
    private Integer problemId;
    private String input;
    private String expectedOutput;
    private Boolean isSample;
    private Integer position;
}
