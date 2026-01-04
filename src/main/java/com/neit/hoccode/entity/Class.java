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
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "text", nullable = false)
    private String title;

    @Column(unique = true, length = 255)
    private String code;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL)
    private List<ClassEnrollment> enrollments = new ArrayList<>();

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();
}