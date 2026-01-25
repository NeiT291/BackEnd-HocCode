package com.neit.hoccode.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1002, "Username is invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH(1012, "Password and re-password not match", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "Password is invalid", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1004, "Invalid message key", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1005, "User not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(1007, "Token is invalid", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED(1008, "You do not have permission", HttpStatus.FORBIDDEN),
    CANNOT_UPLOAD_IMAGE(1009, "Can't upload image", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND(1010, "File not found", HttpStatus.NOT_FOUND),
    REQUEST_FAILED(1011, "Request failed", HttpStatus.BAD_REQUEST),
    COURSE_NOT_FOUND(1012, "Course not found", HttpStatus.NOT_FOUND),
    COURSE_MODULE_NOT_FOUND(1013, "Course not found", HttpStatus.NOT_FOUND),
    COURSE_SLUG_EXISTED(1013, "Course not found", HttpStatus.BAD_REQUEST),
    LESSON_NOT_FOUND(1014, "Lesson not found", HttpStatus.NOT_FOUND),
    PROBLEM_NOT_FOUND(1015, "Problem not found", HttpStatus.NOT_FOUND),
    TESTCASE_NOT_FOUND(1016, "Testcase not found", HttpStatus.NOT_FOUND),
    CLASS_NOT_FOUND(1017, "Class not found", HttpStatus.NOT_FOUND),
    CLASS_CODE_EXIST(1018, "Class code exist", HttpStatus.BAD_REQUEST),
    CONTEST_SLUG_EXITED(1018, "Contest slug exist", HttpStatus.BAD_REQUEST),
    CONTEST_NOT_FOUND(1018, "Contest not found", HttpStatus.NOT_FOUND),
    PROBLEM_NOT_COMPLETED(1019, "Problem not complete", HttpStatus.BAD_REQUEST),
    DO_NOT_HAVE_PERMISSION(1020, "Don't have permission", HttpStatus.BAD_REQUEST),
    ;
    private int code;
    private String message;
    private HttpStatus statusCode;
}
