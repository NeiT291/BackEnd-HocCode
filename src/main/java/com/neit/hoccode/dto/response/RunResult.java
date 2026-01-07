package com.neit.hoccode.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RunResult {
    private String stdout;
    private String stderr;
    private int exitCode;
    private long timeMs;
    private double memoryMb;
}
