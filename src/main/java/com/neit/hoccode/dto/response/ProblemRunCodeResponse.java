package com.neit.hoccode.dto.response;

import com.neit.hoccode.entity.ProblemTestcase;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProblemRunCodeResponse {
    private Integer problemId;
    private String verdict;
    private int passedCount;
    private int totalCount;
    private List<RunCodeResponse> testCaseResult;
}
