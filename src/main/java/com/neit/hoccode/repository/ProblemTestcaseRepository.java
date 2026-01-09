package com.neit.hoccode.repository;

import com.neit.hoccode.entity.Problem;
import com.neit.hoccode.entity.ProblemTestcase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemTestcaseRepository extends JpaRepository<ProblemTestcase, Integer> {
    ProblemTestcase getById(Integer id);
}
