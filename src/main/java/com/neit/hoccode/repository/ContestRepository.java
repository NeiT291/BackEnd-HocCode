package com.neit.hoccode.repository;

import com.neit.hoccode.entity.Class;
import com.neit.hoccode.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Integer> {
    Optional<Contest> getBySlug(String slug);
}
