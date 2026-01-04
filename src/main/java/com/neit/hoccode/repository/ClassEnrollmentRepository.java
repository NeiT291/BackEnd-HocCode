package com.neit.hoccode.repository;

import com.neit.hoccode.entity.Class;
import com.neit.hoccode.entity.ClassEnrollment;
import com.neit.hoccode.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassEnrollmentRepository extends JpaRepository<ClassEnrollment, Integer> {
    ClassEnrollment findByUserAndClassRoom(User user, Class classRoom);
}
