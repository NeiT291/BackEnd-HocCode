package com.neit.hoccode.controller;

import com.neit.hoccode.dto.ApiResponse;
import com.neit.hoccode.dto.request.ContestRequest;
import com.neit.hoccode.dto.response.ContestResponse;
import com.neit.hoccode.dto.response.ResultPaginationResponse;
import com.neit.hoccode.entity.ContestRegistration;
import com.neit.hoccode.service.ContestService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/contest")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Contest API", description = "CRUD cuộc thi")
public class ContestController {
    private final ContestService contestService;

    public ContestController(ContestService contestService) {
        this.contestService = contestService;
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
}
