package com.neit.hoccode.controller;

import com.neit.hoccode.dto.ApiResponse;
import com.neit.hoccode.dto.request.ContestRequest;
import com.neit.hoccode.dto.response.ContestRankingResponse;
import com.neit.hoccode.dto.response.ContestResponse;
import com.neit.hoccode.dto.response.ResultPaginationResponse;
import com.neit.hoccode.entity.ContestRegistration;
import com.neit.hoccode.service.ContestRankingService;
import com.neit.hoccode.service.ContestService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/contest")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Contest API", description = "CRUD cuộc thi")
public class ContestController {
    private final ContestService contestService;
    private final ContestRankingService contestRankingService;

    public ContestController(ContestService contestService, ContestRankingService contestRankingService) {
        this.contestService = contestService;
        this.contestRankingService = contestRankingService;
    }

    @PostMapping("/add")
    public ApiResponse<ContestResponse> add(@RequestBody ContestRequest request){
        return ApiResponse.<ContestResponse>builder().data(contestService.add(request)).build();
    }
    @PutMapping("/modify")
    public ApiResponse<ContestResponse> modify(@RequestBody ContestRequest request){
        return ApiResponse.<ContestResponse>builder().data(contestService.modify(request)).build();
    }
    @GetMapping("/get-all")
    public ApiResponse<ResultPaginationResponse> getAll(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize){
        return ApiResponse.<ResultPaginationResponse>builder().data(contestService.getAll(page, pageSize)).build();
    }
    @GetMapping("/get-by-slug")
    public ApiResponse<ContestResponse> getBySlug(@RequestParam String slug){
        return ApiResponse.<ContestResponse>builder().data(contestService.getBySlug(slug)).build();
    }
    @GetMapping("/enroll")
    public ApiResponse<ContestRegistration> enroll(@RequestParam Integer contestId){
        return ApiResponse.<ContestRegistration>builder().data(contestService.enroll(contestId)).build();
    }
    @GetMapping("/search")
    public ApiResponse<ResultPaginationResponse> getByName(@RequestParam String title,
                                                           @RequestParam("page") Optional<Integer> page,
                                                           @RequestParam("pageSize") Optional<Integer> pageSize
    ) {
        ApiResponse<ResultPaginationResponse> response = new ApiResponse<>();
        response.setData(contestService.getByTitle(title, page, pageSize));
        return response;
    }
    @PostMapping("/set-thumbnail")
    public ApiResponse<Void> setThumbnail(@RequestParam Integer contestId,@RequestParam("thumbnail") MultipartFile thumbnail){
        return ApiResponse.<Void>builder().data(contestService.setThumbnail(contestId, thumbnail)).build();
    }
    @GetMapping("/get-created")
    public ApiResponse<ResultPaginationResponse> getCreated(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize){
        return ApiResponse.<ResultPaginationResponse>builder().data(contestService.getCreated(page, pageSize)).build();
    }
    @GetMapping("/get-joined")
    public ApiResponse<ResultPaginationResponse> getCourseJoined(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize){
        return ApiResponse.<ResultPaginationResponse>builder().data(contestService.getJoined(page, pageSize)).build();
    }
    @GetMapping("/get-by-id")
    public ApiResponse<ContestResponse> getById(@RequestParam Integer id){
        return ApiResponse.<ContestResponse>builder().data(contestService.getById(id)).build();
    }
    @GetMapping("/is-join")
    public ApiResponse<ContestRegistration> isJoined(@RequestParam Integer id){
        return ApiResponse.<ContestRegistration>builder().data(contestService.isJoined(id)).build();
    }
    @DeleteMapping("/delete-contest")
    public ApiResponse<Void> deleteContest(@RequestParam Integer id){
        contestService.deleteContest(id);
        return ApiResponse.<Void>builder().build();
    }
    @GetMapping("/ranking")
    public ApiResponse<ResultPaginationResponse> getRanking(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> pageSize,
            @RequestParam Integer contestId) {

        return ApiResponse.<ResultPaginationResponse>builder()
                .data(contestRankingService.getContestRanking(page,pageSize,contestId)).build();
    }
}
