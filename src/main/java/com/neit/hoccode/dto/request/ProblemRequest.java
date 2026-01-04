package com.neit.hoccode.dto.request;

import com.neit.hoccode.entity.ProblemTestcase;
import com.neit.hoccode.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProblemRequest {
    private Integer id;
    private String title;
    private String slug;
    private String description;
    private Integer contestId;
    private Integer moduleId;
    private Integer timeLimitMs;
    private Integer memoryLimitKb;
    private String difficulty;
    private Boolean isPublic;
    private List<ProblemTestcase> testcases = new ArrayList<>();
}
