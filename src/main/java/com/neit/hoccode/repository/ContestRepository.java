package com.neit.hoccode.repository;

import com.neit.hoccode.entity.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Integer> {
    Optional<Contest> getBySlug(String slug);
    Page<Contest> findByTitleIgnoreCaseContainingAndIsActive(String title, Pageable pageable, Boolean isActive);
    Page<Contest> findAllByIsActive(Pageable pageable, Boolean isActive);

    Page<Contest> findByCreatedByIdAndIsActive(String id,Boolean isActive, Pageable pageable);
}
