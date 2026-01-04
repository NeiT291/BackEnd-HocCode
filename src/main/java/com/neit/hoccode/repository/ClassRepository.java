package com.neit.hoccode.repository;

import com.neit.hoccode.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<Class, Integer> {
    Class findByCode(String code);
}
