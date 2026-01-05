package com.neit.hoccode.controller;

import com.neit.hoccode.dto.ApiResponse;
import com.neit.hoccode.dto.request.ProblemRequest;
import com.neit.hoccode.dto.request.ProblemTestcaseRequest;
import com.neit.hoccode.dto.request.SubmissionRequest;
import com.neit.hoccode.dto.response.ProblemResponse;
import com.neit.hoccode.dto.response.ResultPaginationResponse;
import com.neit.hoccode.entity.CourseModule;
import com.neit.hoccode.entity.JudgeResult;
import com.neit.hoccode.entity.Problem;
import com.neit.hoccode.entity.ProblemTestcase;
import com.neit.hoccode.service.ProblemService;
import com.neit.hoccode.service.ProblemTestcaseService;
import com.neit.hoccode.service.SubmissionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/problems")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Problem API", description = "CRUD problem")
public class ProblemController {
    private final ProblemService problemService;
    private final ProblemTestcaseService problemTestcaseService;
    private final SubmissionService submissionService;

    public ProblemController(ProblemService problemService, ProblemTestcaseService problemTestcaseService, SubmissionService submissionService) {
        this.problemService = problemService;
        this.problemTestcaseService = problemTestcaseService;
        this.submissionService = submissionService;
    }

    @PostMapping("/add")
    public ApiResponse<ProblemResponse> addProblem(@RequestBody ProblemRequest request){
        return ApiResponse.<ProblemResponse>builder().data(problemService.addProblem(request)).build();
    }
    @PutMapping("/modify")
    public ApiResponse<ProblemResponse> modifyProblem(@RequestBody ProblemRequest request){
        return ApiResponse.<ProblemResponse>builder().data(problemService.modifyProblem(request)).build();
    }
    @GetMapping("/get-by-sulg")
    public ApiResponse<ProblemResponse> modifyCourseModule(@RequestParam String slug){
        return ApiResponse.<ProblemResponse>builder().data(problemService.getBySlug(slug)).build();
    }
    @GetMapping("/get-all")
    public ApiResponse<ResultPaginationResponse> getAll(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize, @RequestParam(required = false) String difficulty){
        return ApiResponse.<ResultPaginationResponse>builder().data(problemService.getAll(page, pageSize, difficulty)).build();
    }
    @PostMapping("/add-testcase")
    public ApiResponse<ProblemTestcase> addTestcase(@RequestBody ProblemTestcaseRequest request){
        return ApiResponse.<ProblemTestcase>builder().data(problemTestcaseService.add(request)).build();
    }
    @PutMapping("/modify-testcase")
    public ApiResponse<ProblemTestcase> modifyTestcase(@RequestBody ProblemTestcaseRequest request){
        return ApiResponse.<ProblemTestcase>builder().data(problemTestcaseService.modify(request)).build();
    }
    @PostMapping("/submission")
    public ApiResponse<JudgeResult> submission(@RequestBody SubmissionRequest request){
        return ApiResponse.<JudgeResult>builder().data(submissionService.submission(request)).build();
    }
    @GetMapping("/search")
    public ApiResponse<ResultPaginationResponse> getByName(@RequestParam String title,
                                                           @RequestParam("page") Optional<Integer> page,
                                                           @RequestParam("pageSize") Optional<Integer> pageSize
    ) {
        ApiResponse<ResultPaginationResponse> response = new ApiResponse<>();
        response.setData(problemService.getByTitle(title, page, pageSize));
        return response;
    }
}
