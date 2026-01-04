package com.neit.hoccode.repository;

import com.neit.hoccode.entity.Contest;
import com.neit.hoccode.entity.ContestRegistration;
import com.neit.hoccode.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestRegistrationRepository extends JpaRepository<ContestRegistration, Integer> {
    ContestRegistration findByUserAndContest(User user, Contest contest);
    List<ContestRegistration> findByContest(Contest contest);
}
