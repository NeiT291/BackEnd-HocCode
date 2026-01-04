package com.neit.hoccode.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(length = 100)
    private String displayName;

    private LocalDate dob;

    @Column(columnDefinition = "text")
    private String bio;

    @Column(unique=true)
    private String email;

    private String phone;

    private String address;

    @Column(columnDefinition = "text")
    private String avatarUrl;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "owner")
    private List<Course> ownedCourses = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy")
    private List<Problem> createdProblems = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Submission> submissions = new ArrayList<>();
}
