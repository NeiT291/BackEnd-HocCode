package com.neit.hoccode.repository;

import com.neit.hoccode.entity.Course;
import com.neit.hoccode.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<Course> findBySlug(String slug);
    Course getBySlug(String slug);
    Page<Course> findByTitleIgnoreCaseContaining(String title, Pageable pageable);
}
