package com.neit.hoccode.controller;

import com.neit.hoccode.dto.ApiResponse;
import com.neit.hoccode.dto.response.ResultPaginationResponse;
import com.neit.hoccode.dto.response.admin.DashboardResponse;
import com.neit.hoccode.service.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ApiResponse<DashboardResponse> getDashboard(){
        return ApiResponse.<DashboardResponse>builder().data(adminService.getDashboard()).build();
    }
//  ===================== COURSE ====================
    @GetMapping("/all-course")
    public ApiResponse<ResultPaginationResponse> getAllCourse(@RequestParam Optional<Integer> page,
                                                        @RequestParam Optional<Integer> pageSize,
                                                        @RequestParam(required = false) String title,
                                                        @RequestParam(required = false) Boolean isActive){
        return ApiResponse.<ResultPaginationResponse>builder().data(adminService.getAllCourse(page, pageSize, title, isActive)).build();
    }
    @DeleteMapping("/delete-courses")
    public ApiResponse<Void> deleteCourse(@RequestParam List<Integer> listCourseId){
        adminService.deleteCourses(listCourseId);
        return ApiResponse.<Void>builder().build();
    }
    @GetMapping("/restore-courses")
    public ApiResponse<Void> restoreCourse(@RequestParam List<Integer> listCourseId){
        adminService.restoreCourses(listCourseId);
        return ApiResponse.<Void>builder().build();
    }
//  ===================== Problem ====================
    @GetMapping("/all-problem")
    public ApiResponse<ResultPaginationResponse> getAllProblem(@RequestParam Optional<Integer> page,
                                                        @RequestParam Optional<Integer> pageSize,
                                                        @RequestParam(required = false) String title,
                                                        @RequestParam(required = false) Boolean isActive){
        return ApiResponse.<ResultPaginationResponse>builder().data(adminService.getAllProblem(page, pageSize, title, isActive)).build();
    }
    @DeleteMapping("/delete-problems")
    public ApiResponse<Void> deleteProblem(@RequestParam List<Integer> listProblemId){
        adminService.deleteProblems(listProblemId);
        return ApiResponse.<Void>builder().build();
    }
    @GetMapping("/restore-problems")
    public ApiResponse<Void> restoreProblem(@RequestParam List<Integer> listProblemId){
        adminService.restoreProblems(listProblemId);
        return ApiResponse.<Void>builder().build();
    }
//  ===================== Contest ====================
    @GetMapping("/all-contest")
    public ApiResponse<ResultPaginationResponse> getAllContest(@RequestParam Optional<Integer> page,
                                                               @RequestParam Optional<Integer> pageSize,
                                                               @RequestParam(required = false) String title,
                                                               @RequestParam(required = false) Boolean isActive){
        return ApiResponse.<ResultPaginationResponse>builder().data(adminService.getAllContest(page, pageSize, title, isActive)).build();
    }
    @DeleteMapping("/delete-contests")
    public ApiResponse<Void> deleteContests(@RequestParam List<Integer> listContestId){
        adminService.deleteContests(listContestId);
        return ApiResponse.<Void>builder().build();
    }
    @GetMapping("/restore-contests")
    public ApiResponse<Void> restoreContests(@RequestParam List<Integer> listContestId){
        adminService.restoreContests(listContestId);
        return ApiResponse.<Void>builder().build();
    }
//  ===================== User ====================
    @GetMapping("/all-user")
    public ApiResponse<ResultPaginationResponse> getAllUser(@RequestParam Optional<Integer> page,
                                                               @RequestParam Optional<Integer> pageSize,
                                                               @RequestParam(required = false) String username,
                                                               @RequestParam(required = false) Boolean isActive){
        return ApiResponse.<ResultPaginationResponse>builder().data(adminService.getAllUser(page, pageSize, username, isActive)).build();
    }
    @DeleteMapping("/delete-users")
    public ApiResponse<Void> deleteUsers(@RequestParam List<String> listUsername){
        adminService.deleteUsers(listUsername);
        return ApiResponse.<Void>builder().build();
    }
    @GetMapping("/restore-users")
    public ApiResponse<Void> restoreUsers(@RequestParam List<String> listUsername){
        adminService.restoreUsers(listUsername);
        return ApiResponse.<Void>builder().build();
    }
}
