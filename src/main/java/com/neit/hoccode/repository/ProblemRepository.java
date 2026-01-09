package com.neit.hoccode.repository;

import com.neit.hoccode.entity.Contest;
import com.neit.hoccode.entity.Course;
import com.neit.hoccode.entity.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {
    Optional<Problem> findBySlug(String slug);
    Page<Problem> findAllByDifficulty(Pageable pageable, String difficulty);
    Page<Problem> findByTitleIgnoreCaseContaining(String title, Pageable pageable);

    Page<Problem> findByCreatedByIdAndIsTheory(String id,boolean isTheory, Pageable pageable);
}
