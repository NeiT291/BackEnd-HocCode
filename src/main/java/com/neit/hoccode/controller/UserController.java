package com.neit.hoccode.controller;

import com.neit.hoccode.dto.ApiResponse;
import com.neit.hoccode.dto.request.RegisterRequest;
import com.neit.hoccode.dto.request.UpdateUserRequest;
import com.neit.hoccode.dto.response.UserResponse;
import com.neit.hoccode.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User API", description = "CRUD User")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody RegisterRequest request) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.register(request))
                .build();
    }
    @GetMapping("/my-info")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .data(userService.getMyInfo())
                .build();
    }
    @PutMapping("/update")
    public ApiResponse<UserResponse> update(@RequestBody UpdateUserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.updateInfo(request))
                .build();
    }
    @PostMapping("/set-avatar")
    public ApiResponse<Void> setThumbnail(@RequestParam("avatar") MultipartFile avatar){
        return ApiResponse.<Void>builder().data(userService.setAvatar(avatar)).build();
    }
    @DeleteMapping("/deActive")
    public ApiResponse<Void> deActive(@RequestParam String username){
        userService.deActiveUser(username);
        return ApiResponse.<Void>builder().build();
    }
}
