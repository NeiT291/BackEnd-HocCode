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
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "text", nullable = false)
    private String title;

    private String slug;

    @Column(columnDefinition = "text")
    private String description;

    @Builder.Default
    private Integer position = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer timeLimitMs = 2000;

    @Column(nullable = false)
    @Builder.Default
    private Integer memoryLimitKb = 65536;

    private String difficulty;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnore
    private User createdBy;

    @Builder.Default
    private Boolean isPublic = true;

    private LocalDateTime createdAt;

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder.Default
    private Boolean isTheory = true;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
    private List<ProblemTestcase> testcases = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "module_id")
    @JsonIgnore
    private CourseModule module;

    @ManyToOne
    @JoinColumn(name = "contest_id")
    @JsonIgnore
    private Contest contest;
}
