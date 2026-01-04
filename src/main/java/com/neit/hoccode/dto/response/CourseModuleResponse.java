package com.neit.hoccode.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neit.hoccode.entity.Course;
import com.neit.hoccode.entity.Lesson;
import com.neit.hoccode.entity.Problem;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CourseModuleResponse {
    private Integer id;
    private String title;
    private Integer position = 0;
    private LocalDateTime createdAt;
    private List<Lesson> lessons = new ArrayList<>();
    private List<ProblemResponse> problems = new ArrayList<>();
}
