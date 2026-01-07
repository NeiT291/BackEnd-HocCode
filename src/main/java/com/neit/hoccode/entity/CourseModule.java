package com.neit.hoccode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    private Course course;

    @Column(columnDefinition = "text", nullable = false)
    private String title;

    private Integer position = 0;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
    private List<Problem> problems = new ArrayList<>();
}