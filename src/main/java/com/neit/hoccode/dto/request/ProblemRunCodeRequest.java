package com.neit.hoccode.dto.request;

import lombok.Data;

@Data
public class ProblemRunCodeRequest {
    private Integer problemId;
    private Integer languageId;
    private String language;
    private String sourceCode;
    private String input;
}
