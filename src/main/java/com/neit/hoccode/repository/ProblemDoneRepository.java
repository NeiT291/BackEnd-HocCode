package com.neit.hoccode.repository;

import com.neit.hoccode.entity.CourseProgress;
import com.neit.hoccode.entity.ProblemDone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemDoneRepository extends JpaRepository<ProblemDone, Integer> {
    List<ProblemDone> findByUserId(String userId);
    Optional<ProblemDone> findByUserIdAndProblemId(String userId, Integer problemId);
}
