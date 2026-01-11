package com.neit.hoccode.controller;

import com.neit.hoccode.dto.ApiResponse;
import com.neit.hoccode.dto.request.CourseRequest;
import com.neit.hoccode.dto.response.CourseResponse;
import com.neit.hoccode.dto.response.ResultPaginationResponse;
import com.neit.hoccode.entity.CourseEnrollment;
import com.neit.hoccode.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/add")
    public ApiResponse<CourseResponse> addCourse(@RequestBody CourseRequest request){
        return ApiResponse.<CourseResponse>builder().data(courseService.addCourse(request)).build();
    }
    @PostMapping("/set-thumbnail")
    public ApiResponse<Void> setThumbnail(@RequestParam Integer courseId,@RequestParam("thumbnail") MultipartFile thumbnail){
        return ApiResponse.<Void>builder().data(courseService.setThumbnail(courseId, thumbnail)).build();
    }
    @PutMapping("/modify")
    public ApiResponse<CourseResponse> modifyCourse(@RequestBody CourseRequest request){
        return ApiResponse.<CourseResponse>builder().data(courseService.modifyCourse(request)).build();
    }
    @GetMapping("/get-by-id")
    public ApiResponse<CourseResponse> getCourseById(@RequestParam Integer id){
        return ApiResponse.<CourseResponse>builder().data(courseService.getCourseById(id)).build();
    }
    @GetMapping("/get-by-slug")
    public ApiResponse<CourseResponse> getCourseBySlug(@RequestParam String slug){
        return ApiResponse.<CourseResponse>builder().data(courseService.getCourseBySlug(slug)).build();
    }
    @GetMapping("/get-all")
    public ApiResponse<ResultPaginationResponse> getAll(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize){
        return ApiResponse.<ResultPaginationResponse>builder().data(courseService.getAll(page, pageSize)).build();
    }
    @PostMapping("/enroll")
    public ApiResponse<CourseEnrollment> enRoll(@RequestParam Integer courseId){
        return ApiResponse.<CourseEnrollment>builder().data(courseService.enRoll(courseId)).build();
    }
    @PostMapping("/out-course")
    public ApiResponse<?> outCourse(@RequestParam Integer courseId){
        courseService.outCourse(courseId);
        return ApiResponse.builder().build();
    }
    @GetMapping("/search")
    public ApiResponse<ResultPaginationResponse> getByName(@RequestParam String title,
                                                           @RequestParam("page") Optional<Integer> page,
                                                           @RequestParam("pageSize") Optional<Integer> pageSize
    ) {
        ApiResponse<ResultPaginationResponse> response = new ApiResponse<>();
        response.setData(courseService.getByTitle(title, page, pageSize));
        return response;
    }
    @GetMapping("/is-join")
    public ApiResponse<CourseEnrollment> isJoin(@RequestParam Integer courseId){
        return ApiResponse.<CourseEnrollment>builder().data(courseService.isJoin(courseId)).build();
    }
    @GetMapping("/get-course-created")
    public ApiResponse<ResultPaginationResponse> getCourseCreated(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize){
        return ApiResponse.<ResultPaginationResponse>builder().data(courseService.getCourseCreated(page, pageSize)).build();
    }
    @GetMapping("/get-course-joined")
    public ApiResponse<ResultPaginationResponse> getCourseJoined(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize){
        return ApiResponse.<ResultPaginationResponse>builder().data(courseService.getCourseJoined(page, pageSize)).build();
    }
    @DeleteMapping("/delete-course")
    public ApiResponse<Void> deleteCourse(@RequestParam Integer id){
        return ApiResponse.<Void>builder().build();
    }
}
