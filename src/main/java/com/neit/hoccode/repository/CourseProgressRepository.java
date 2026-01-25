package com.neit.hoccode.repository;

import com.neit.hoccode.entity.CourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseProgressRepository extends JpaRepository<CourseProgress, Integer> {
    Optional<CourseProgress> findByUserIdAndProblemId(String userId, Integer problemId);
    List<CourseProgress> findByUserIdAndCourseId(String userId, Integer courseId);

}
