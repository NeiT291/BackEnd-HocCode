package com.neit.hoccode.entity;

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
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Column(columnDefinition = "text", nullable = false)
    private String language;

    @Column(columnDefinition = "text", nullable = false)
    private String sourceCode;

    private LocalDateTime createdAt;

    @Column(length = 20)
    private String verdict;

    private Integer totalTimeMs;

    private Integer totalMemoryKb;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JudgeResult> judgeResults = new ArrayList<>();
}
