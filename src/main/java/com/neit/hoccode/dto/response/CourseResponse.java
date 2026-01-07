package com.neit.hoccode.dto.response;

import com.neit.hoccode.entity.CourseModule;
import com.neit.hoccode.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class CourseResponse {
    private Integer id;
    private String thumbnailUrl;
    private String title;
    private String slug;
    private String description;
    private UserResponse owner;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CourseModuleResponse> modules;
}
