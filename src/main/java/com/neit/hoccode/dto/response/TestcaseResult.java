package com.neit.hoccode.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestcaseResult {

    private Integer testcaseId;
    private String status;          // PASS / FAIL / TLE / RE
    private String input;
    private String expectedOutput;
    private String actualOutput;

    private String time;
    private Integer memory;       // timeout, runtime error...
}