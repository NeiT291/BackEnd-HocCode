package com.neit.hoccode.dto.request;

import lombok.Data;

@Data
public class SubmissionRequest {
    private Integer problemId;
    private Integer testcaseId;
    private String language;
    private String sourceCode;
}
