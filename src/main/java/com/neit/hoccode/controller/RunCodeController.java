package com.neit.hoccode.controller;

import com.neit.hoccode.dto.ApiResponse;
import com.neit.hoccode.dto.request.RunRequest;
import com.neit.hoccode.dto.response.RunResult;
import com.neit.hoccode.service.DockerCodeRunnerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/run-code")
@SecurityRequirement(name = "bearerAuth")
public class RunCodeController {
    private final DockerCodeRunnerService dockerCodeRunnerService;

    public RunCodeController(DockerCodeRunnerService dockerCodeRunnerService) {
        this.dockerCodeRunnerService = dockerCodeRunnerService;
    }

    @PostMapping
    public ApiResponse<RunResult> runCode(@RequestBody RunRequest request){
        RunResult runResult = dockerCodeRunnerService.run(request.getLanguage(),request.getCode());
        return ApiResponse.<RunResult>builder()
                .data(runResult)
                .build();
    }
}
