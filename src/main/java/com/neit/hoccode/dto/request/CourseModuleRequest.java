package com.neit.hoccode.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neit.hoccode.entity.Course;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CourseModuleRequest {
    private int id;
    private int courseId;
    private String title;
    private Integer position = 0;
    private LocalDateTime createdAt;
}
