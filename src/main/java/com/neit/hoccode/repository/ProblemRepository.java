package com.neit.hoccode.repository;

import com.neit.hoccode.entity.Contest;
import com.neit.hoccode.entity.Course;
import com.neit.hoccode.entity.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {
    Optional<Problem> findBySlug(String slug);
    Page<Problem> findAllByDifficultyAndIsPublicAndIsActive(Pageable pageable, String difficulty, boolean isPublic, boolean isActive);
    Page<Problem> findByTitleIgnoreCaseContainingAndIsPublicAndIsActive(String title, Pageable pageable,Boolean isPublic, Boolean isActive);
    Page<Problem> findAllByIsPublicAndIsActive(Pageable pageable, Boolean isPublic, Boolean isActive);
    List<Problem> findAllByIsPublic(Boolean isPublic);
    Page<Problem> findByCreatedByIdAndIsTheoryAndIsPublicAndIsActive(String id,Boolean isTheory, Pageable pageable, Boolean isPublic, Boolean isActive);
    Long countByIsPublic(Boolean isPublic);
    List<Problem> findByContestIdAndIsActiveTrue(Integer contestId);
}
