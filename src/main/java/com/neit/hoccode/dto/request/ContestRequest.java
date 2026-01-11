package com.neit.hoccode.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.neit.hoccode.entity.Problem;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ContestRequest {
    private Integer id;
    private String title;
    private String slug;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
