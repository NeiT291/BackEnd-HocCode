package com.neit.hoccode.controller;

import com.neit.hoccode.dto.ApiResponse;
import com.neit.hoccode.dto.request.ProblemRunCodeRequest;
import com.neit.hoccode.dto.response.RunCodeResponse;
import com.neit.hoccode.dto.response.RunResult;
import com.neit.hoccode.service.RunCodeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/run-code")
@RequiredArgsConstructor
public class RunCodeController {

    private final RunCodeService runCodeService;

    @PostMapping
    public ApiResponse<RunCodeResponse> run(@RequestBody ProblemRunCodeRequest request) {
        return ApiResponse.<RunCodeResponse>builder().data(runCodeService.runApiTest(
                request.getLanguageId(),
                request.getSourceCode(),
                request.getInput()
        )).build();
    }
}

