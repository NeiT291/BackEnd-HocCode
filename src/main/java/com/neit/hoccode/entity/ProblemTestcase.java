package com.neit.hoccode.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemTestcase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    @JsonIgnore
    private Problem problem;

    @Column(columnDefinition = "text")
    private String input;

    @Column(columnDefinition = "text")
    private String expectedOutput;

    @Builder.Default
    private Boolean isSample = false;

    @Builder.Default
    private Integer position = 0;
}
