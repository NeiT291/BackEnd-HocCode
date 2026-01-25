package com.neit.hoccode.dto.response;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CourseProgressResponse {
    private Integer problemId;
    private String status;
}

