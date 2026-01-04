package com.neit.hoccode.repository;

import com.neit.hoccode.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {
    Optional<Problem> findBySlug(String slug);
}
