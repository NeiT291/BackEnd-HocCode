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
public interface ClassRepository extends JpaRepository<Class, Integer> {
    Class findByCode(String code);
    Page<Class> findByTitleIgnoreCaseContaining(String title, Pageable pageable);

    Page<Class> findByOwnerId(String id, Pageable pageable);
}
