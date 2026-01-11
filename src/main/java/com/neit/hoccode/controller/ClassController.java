package com.neit.hoccode.controller;


import com.neit.hoccode.dto.ApiResponse;
import com.neit.hoccode.dto.request.ClassRequest;
import com.neit.hoccode.dto.response.ClassResponse;
import com.neit.hoccode.dto.response.ResultPaginationResponse;
import com.neit.hoccode.service.ClassService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/class")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Class API", description = "CRUD lớp học")
public class ClassController {
    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @PostMapping("/add")
    public ApiResponse<ClassResponse> add(@RequestBody ClassRequest request){
        return ApiResponse.<ClassResponse>builder().data(classService.add(request)).build();
    }
    @PutMapping("/modify")
    public ApiResponse<ClassResponse> modify(@RequestBody ClassRequest request){
        return ApiResponse.<ClassResponse>builder().data(classService.modify(request)).build();
    }
    @GetMapping("/enroll")
    public ApiResponse<?> enroll(@RequestParam Integer classId){
        classService.enroll(classId);
        return ApiResponse.builder().build();
    }
    @GetMapping("/out-class")
    public ApiResponse<?> outClass(@RequestParam Integer classId){
        classService.outClass(classId);
        return ApiResponse.builder().build();
    }
    @GetMapping("/get-all")
    public ApiResponse<ResultPaginationResponse> getAll(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize){
        return ApiResponse.<ResultPaginationResponse>builder().data(classService.getAll(page, pageSize)).build();
    }

    @GetMapping("/search")
    public ApiResponse<ResultPaginationResponse> getByName(@RequestParam String title,
                                                           @RequestParam("page") Optional<Integer> page,
                                                           @RequestParam("pageSize") Optional<Integer> pageSize
    ) {
        ApiResponse<ResultPaginationResponse> response = new ApiResponse<>();
        response.setData(classService.getByTitle(title, page, pageSize));
        return response;
    }
    @GetMapping("/get-created")
    public ApiResponse<ResultPaginationResponse> getCreated(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize){
        return ApiResponse.<ResultPaginationResponse>builder().data(classService.getCreated(page, pageSize)).build();
    }
    @GetMapping("/get-joined")
    public ApiResponse<ResultPaginationResponse> getCourseJoined(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize){
        return ApiResponse.<ResultPaginationResponse>builder().data(classService.getJoined(page, pageSize)).build();
    }
    @DeleteMapping("/delete-class")
    public ApiResponse<Void> deleteClass(@RequestParam Integer id){
        return ApiResponse.<Void>builder().build();
    }
}
