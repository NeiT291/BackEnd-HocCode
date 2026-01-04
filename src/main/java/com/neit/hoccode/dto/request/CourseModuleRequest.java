package com.neit.hoccode.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neit.hoccode.entity.Course;
import com.neit.hoccode.entity.Lesson;
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
    private List<Lesson> lessons = new ArrayList<>();
}
