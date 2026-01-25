package com.neit.hoccode.repository;

import com.neit.hoccode.entity.Submission;
import com.neit.hoccode.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
    @Query("""
    SELECT s
    FROM Submission s
    WHERE s.user.id = :userId
      AND s.problem.id = :problemId
      AND s.verdict = 'Accepted'
      AND s.createdAt BETWEEN :start AND :end
    ORDER BY s.createdAt ASC
""")
    List<Submission> findAcceptedSubmissions(
            @Param("userId") String userId,
            @Param("problemId") Integer problemId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
    @Query("""
    SELECT COUNT(s)
    FROM Submission s
    WHERE s.user.id = :userId
      AND s.problem.id = :problemId
      AND s.verdict <> 'Accepted'
      AND s.createdAt < :acceptedTime
""")
    Integer countWrongBeforeAccepted(
            @Param("userId") String userId,
            @Param("problemId") Integer problemId,
            @Param("acceptedTime") LocalDateTime acceptedTime
    );
}
