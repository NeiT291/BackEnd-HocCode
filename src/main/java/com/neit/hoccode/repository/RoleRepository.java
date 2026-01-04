package com.neit.hoccode.repository;

import com.neit.hoccode.entity.Role;
import com.neit.hoccode.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
