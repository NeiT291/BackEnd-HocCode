package com.neit.hoccode.dto.response;

import lombok.Data;

@Data
public class RunCodeResponse {
    private Integer testCaseId;
    private String stdout;
    private String stderr;
    private String compileOutput;

    private String time;          // CPU time
    private Integer memory;       // memory (KB)

    private Integer statusId;
    private String status;

    private String message;       // internal error / info
}
