package com.neit.hoccode.controller;

import com.neit.hoccode.dto.ApiResponse;
import com.neit.hoccode.entity.Lesson;
import com.neit.hoccode.service.LessonService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tests")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Test API")
public class TestController {
    private final LessonService lessonService;

    public TestController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public ApiResponse<String> test(){

        return ApiResponse.<String>builder()
                .data("Test success!!!")
                .build();
    }
}
