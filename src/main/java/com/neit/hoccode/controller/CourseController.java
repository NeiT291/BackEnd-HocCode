package com.neit.hoccode.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.neit.hoccode.dto.ApiResponse;
import com.neit.hoccode.dto.request.CourseModuleRequest;
import com.neit.hoccode.dto.request.CourseRequest;
import com.neit.hoccode.dto.request.LessonRequest;
import com.neit.hoccode.dto.response.CourseResponse;
import com.neit.hoccode.dto.response.ResultPaginationResponse;
import com.neit.hoccode.entity.Course;
import com.neit.hoccode.entity.CourseEnrollment;
import com.neit.hoccode.entity.CourseModule;
import com.neit.hoccode.entity.Lesson;
import com.neit.hoccode.service.CourseModuleService;
import com.neit.hoccode.service.CourseService;
import com.neit.hoccode.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/course")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Course API", description = "CRUD khóa học")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }
//  =========== API COURSE ===================
    @Operation(
            summary = "Thêm khóa học",
            description = "API trả về thông tin khóa học đã thêm"
    )
    @PostMapping("/add")
    public ApiResponse<CourseResponse> addCourse(@RequestBody CourseRequest request){
        return ApiResponse.<CourseResponse>builder().data(courseService.addCourse(request)).build();
    }
    @Operation(
            summary = "Sửa thông tin khóa học",
            description = "API trả về thông tin khóa học đã sửa"
    )
    @PutMapping("/modify")
    public ApiResponse<CourseResponse> modifyCourse(@RequestBody CourseRequest request){
        return ApiResponse.<CourseResponse>builder().data(courseService.modifyCourse(request)).build();
    }
    @Operation(
            summary = "Lấy thông tin khóa học bằng slug",
            description = "API trả về thông tin khóa học theo slug"
    )
    @GetMapping("/get-by-slug")
    public ApiResponse<CourseResponse> getCourseBySlug(@RequestParam String slug){
        return ApiResponse.<CourseResponse>builder().data(courseService.getCourseBySlug(slug)).build();
    }
    @GetMapping("/get-all")
    public ApiResponse<ResultPaginationResponse> getAll(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize){
        return ApiResponse.<ResultPaginationResponse>builder().data(courseService.getAll(page, pageSize)).build();
    }
    @GetMapping("/enroll")
    public ApiResponse<CourseEnrollment> enRoll(@RequestParam Integer courseId){
        return ApiResponse.<CourseEnrollment>builder().data(courseService.enRoll(courseId)).build();
    }
    @GetMapping("/out-course")
    public ApiResponse<?> outCourse(@RequestParam Integer courseId){
        courseService.outCourse(courseId);
        return ApiResponse.builder().build();
    }
}
