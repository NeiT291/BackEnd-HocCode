package com.neit.hoccode.dto.response;

import com.neit.hoccode.entity.ClassEnrollment;
import com.neit.hoccode.entity.Course;
import com.neit.hoccode.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ClassResponse {
    private Integer id;
    private String title;
    private String code;
    private String description;
    private UserResponse owner;
    private LocalDateTime createdAt;
    private List<ClassEnrollment> enrollments = new ArrayList<>();
    private List<CourseResponse> courses = new ArrayList<>();
}
