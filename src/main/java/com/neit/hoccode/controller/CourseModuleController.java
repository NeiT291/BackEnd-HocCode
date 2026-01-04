package com.neit.hoccode.controller;

import com.neit.hoccode.dto.ApiResponse;
import com.neit.hoccode.dto.request.CourseModuleRequest;
import com.neit.hoccode.dto.response.CourseModuleResponse;
import com.neit.hoccode.entity.CourseModule;
import com.neit.hoccode.service.CourseModuleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/course-module")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Course module API", description = "CRUD module khóa học")
public class CourseModuleController {
    private final CourseModuleService courseModuleService;

    public CourseModuleController(CourseModuleService courseModuleService) {
        this.courseModuleService = courseModuleService;
    }
//  =========== API COURSE MODULE ===================
    @PostMapping("/add")
    public ApiResponse<CourseModuleResponse> addCourseModule(@RequestBody CourseModuleRequest request){
        return ApiResponse.<CourseModuleResponse>builder().data(courseModuleService.addCourseModule(request)).build();
    }
    @PutMapping("/modify")
    public ApiResponse<CourseModuleResponse> modifyCourseModule(@RequestBody CourseModuleRequest request){
        return ApiResponse.<CourseModuleResponse>builder().data(courseModuleService.modifyCourseModule(request)).build();
    }
    @GetMapping("/get-by-course")
    public ApiResponse<List<CourseModuleResponse>> modifyCourseModule(@RequestParam Integer course_id){
        return ApiResponse.<List<CourseModuleResponse>>builder().data(courseModuleService.getByCourseId(course_id)).build();
    }
}
