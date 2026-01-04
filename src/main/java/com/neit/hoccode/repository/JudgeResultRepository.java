package com.neit.hoccode.repository;

import com.neit.hoccode.entity.CourseModule;
import com.neit.hoccode.entity.JudgeResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JudgeResultRepository extends JpaRepository<JudgeResult, Integer> {
}
