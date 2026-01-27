package com.neit.hoccode.exception;

import com.neit.hoccode.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.nio.file.AccessDeniedException;
import java.text.ParseException;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> runtimeExceptionHandler(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> accessDeniedExceptionHandler(AccessDeniedException exception) {
        System.out.println(exception.getMessage());
        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handlingValidation(MethodArgumentNotValidException exception) {
        System.out.println(exception.getMessage());
        String enumKey = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
            System.out.println("Loi o Global Exception");
        }
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }
    @ExceptionHandler(value = ParseException.class)
    public ResponseEntity<ApiResponse<?>> handlingParseException(ParseException exception) {
        System.out.println(exception.getMessage());
        ErrorCode errorCode = ErrorCode.REQUEST_FAILED;
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
    @ExceptionHandler(value = WebClientResponseException.class)
    public ResponseEntity<ApiResponse<?>> handlingWebClientException(WebClientResponseException exception) {
        System.out.println(exception.getMessage());
        ErrorCode errorCode = ErrorCode.SYNTAX_ERROR;
        return ResponseEntity
                .ok()
//                .status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
}
