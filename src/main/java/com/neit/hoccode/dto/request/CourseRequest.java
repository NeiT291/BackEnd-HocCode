package com.neit.hoccode.dto.request;


import com.neit.hoccode.entity.CourseModule;
import com.neit.hoccode.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class CourseRequest {
    private Integer id;
    private Integer classId;
    private String title;
    private String slug;
    private String description;
    private Boolean isPublic;
    private List<CourseModule> modules;
}
