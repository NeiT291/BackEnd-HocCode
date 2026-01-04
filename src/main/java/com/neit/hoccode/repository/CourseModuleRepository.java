package com.neit.hoccode.repository;

import com.neit.hoccode.entity.Course;
import com.neit.hoccode.entity.CourseEnrollment;
import com.neit.hoccode.entity.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseModuleRepository extends JpaRepository<CourseModule, Integer> {
    List<CourseModule> findByCourse(Course course);
}
