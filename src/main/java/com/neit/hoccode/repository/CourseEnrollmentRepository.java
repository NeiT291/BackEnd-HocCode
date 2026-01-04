package com.neit.hoccode.repository;

import com.neit.hoccode.entity.Contest;
import com.neit.hoccode.entity.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Integer> {
    CourseEnrollment findByCourseId(int classId);
}
