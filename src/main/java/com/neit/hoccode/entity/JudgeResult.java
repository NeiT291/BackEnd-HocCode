package com.neit.hoccode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JudgeResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "submission_id", nullable = false)
    @JsonIgnore
    private Submission submission;

    @ManyToOne
    @JoinColumn(name = "testcase_id", nullable = false)
    @JsonIgnore
    private ProblemTestcase testcase;

    @Column(length = 20)
    private String verdict;

    private Integer timeMs;

    private Integer memoryKb;

    @Column(columnDefinition = "text")
    private String output;

    @Column(columnDefinition = "text")
    private String error;
}