package com.neit.hoccode.dto.response;

import com.neit.hoccode.entity.Problem;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ContestResponse {
    private Integer id;
    private String thumbnailUrl;
    private String title;
    private String slug;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private UserResponse createdBy;
    private List<UserResponse> userEnroll;
    private List<ProblemResponse> problems;
    private LocalDateTime createdAt;
}
