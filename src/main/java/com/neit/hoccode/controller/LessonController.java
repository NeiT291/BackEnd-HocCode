package com.neit.hoccode.controller;

import com.neit.hoccode.dto.ApiResponse;
import com.neit.hoccode.dto.request.LessonRequest;
import com.neit.hoccode.dto.response.ResultPaginationResponse;
import com.neit.hoccode.entity.Lesson;
import com.neit.hoccode.service.LessonService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/lessons")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Lesson API", description = "CRUD lesson")
public class LessonController {
    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }
//  =========== API LESSON ===================
    @PostMapping("/add-lesson")
    public ApiResponse<Lesson> addLesson(@RequestBody LessonRequest request){
        return ApiResponse.<Lesson>builder().data(lessonService.addLesson(request)).build();
    }
    @PutMapping("/modify-lesson")
    public ApiResponse<Lesson> modifyLesson(@RequestBody LessonRequest request){
        return ApiResponse.<Lesson>builder().data(lessonService.modifyLesson(request)).build();
    }
    @GetMapping("/get-all")
    public ApiResponse<ResultPaginationResponse> getAll(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize){
        return ApiResponse.<ResultPaginationResponse>builder().data(lessonService.getAll(page, pageSize)).build();
    }
}
