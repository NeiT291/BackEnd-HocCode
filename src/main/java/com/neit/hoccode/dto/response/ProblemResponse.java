package com.neit.hoccode.dto.response;

import com.neit.hoccode.entity.Contest;
import com.neit.hoccode.entity.CourseModule;
import com.neit.hoccode.entity.ProblemTestcase;
import com.neit.hoccode.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProblemResponse {
    private Integer id;
    private String title;
    private String slug;
    private String description;
    private Integer timeLimitMs;
    private Integer memoryLimitKb;
    private String difficulty;
    private UserResponse createdBy;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isTheory;
    private Integer position;
    private List<ProblemTestcase> testcases = new ArrayList<>();
    private Boolean isActive;
}
