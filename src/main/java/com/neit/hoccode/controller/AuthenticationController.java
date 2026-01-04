package com.neit.hoccode.controller;

import com.neit.hoccode.dto.ApiResponse;
import com.neit.hoccode.dto.request.IntrospectRequest;
import com.neit.hoccode.dto.request.LoginRequest;
import com.neit.hoccode.dto.request.LogoutRequest;
import com.neit.hoccode.dto.request.RefreshRequest;
import com.neit.hoccode.dto.response.AuthenticationResponse;
import com.neit.hoccode.dto.response.IntrospectResponse;
import com.neit.hoccode.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth API")
public class AuthenticationController {
    AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticationService.authenticate(request))
                .build();
    }
    @PostMapping("/logout")
    public ApiResponse<Void> login(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }
    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> login(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticationService.refreshToken(request))
                .build();
    }
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .data(authenticationService.introspect(request))
                .build();
    }
}
