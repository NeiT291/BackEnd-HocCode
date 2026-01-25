package com.neit.hoccode.controller;

import com.neit.hoccode.dto.ApiResponse;
import com.neit.hoccode.dto.request.ProblemRequest;
import com.neit.hoccode.dto.request.ProblemRunCodeRequest;
import com.neit.hoccode.dto.request.ProblemTestcaseRequest;
import com.neit.hoccode.dto.response.ProblemResponse;
import com.neit.hoccode.dto.response.ProblemRunCodeResponse;
import com.neit.hoccode.dto.response.ResultPaginationResponse;
import com.neit.hoccode.entity.ProblemTestcase;
import com.neit.hoccode.service.ProblemService;
import com.neit.hoccode.service.ProblemTestcaseService;
import com.neit.hoccode.service.SubmissionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/get-by-id")
    public ApiResponse<ProblemResponse> modifyCourseModule(@RequestParam Integer id){
        return ApiResponse.<ProblemResponse>builder().data(problemService.getById(id)).build();
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
    @GetMapping("/search")
    public ApiResponse<ResultPaginationResponse> getByName(@RequestParam String title,
                                                           @RequestParam("page") Optional<Integer> page,
                                                           @RequestParam("pageSize") Optional<Integer> pageSize
    ) {
        ApiResponse<ResultPaginationResponse> response = new ApiResponse<>();
        response.setData(problemService.getByTitle(title, page, pageSize));
        return response;
    }
    @DeleteMapping("/delete-testcase")
    public ApiResponse<Void> deleteTestcase(@RequestParam Integer id){
        problemTestcaseService.deleteTestcase(id);
        return ApiResponse.<Void>builder().build();
    }
    @DeleteMapping("/delete-problem")
    public ApiResponse<Void> deleteProblem(@RequestParam Integer id){
        problemService.deleteProblem(id);
        return ApiResponse.<Void>builder().build();
    }
    @GetMapping("/get-created")
    public ApiResponse<ResultPaginationResponse> getCreated(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize){
        return ApiResponse.<ResultPaginationResponse>builder().data(problemService.getCreated(page, pageSize)).build();
    }
    @PostMapping("/run-test")
    public ApiResponse<ProblemRunCodeResponse> runTest(@RequestBody ProblemRunCodeRequest request){
        return ApiResponse.<ProblemRunCodeResponse>builder().data(problemService.runTest(request)).build();
    }
    @PostMapping("/submit")
    public ApiResponse<ProblemRunCodeResponse> submit(@RequestBody ProblemRunCodeRequest request){
        return ApiResponse.<ProblemRunCodeResponse>builder().data(problemService.submitCode(request)).build();
    }
    @GetMapping("/get-dones")
    public ApiResponse<ResultPaginationResponse> getDone(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize, @RequestParam(required = false) String difficulty){
        return ApiResponse.<ResultPaginationResponse>builder().data(problemService.getListDone(page, pageSize, difficulty)).build();
    }
    @GetMapping("/isDone")
    public ApiResponse<Boolean> isDone(@RequestParam Integer problemId){
        return ApiResponse.<Boolean>builder().data(problemService.isDone(problemId)).build();
    }
}
