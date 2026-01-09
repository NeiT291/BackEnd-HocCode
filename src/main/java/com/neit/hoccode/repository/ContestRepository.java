package com.neit.hoccode.repository;

import com.neit.hoccode.entity.Class;
import com.neit.hoccode.entity.Contest;
import com.neit.hoccode.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Integer> {
    Optional<Contest> getBySlug(String slug);
    Page<Contest> findByTitleIgnoreCaseContaining(String title, Pageable pageable);

    Page<Contest> findByCreatedById(String id, Pageable pageable);
}
