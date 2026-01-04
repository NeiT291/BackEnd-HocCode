package com.neit.hoccode.service;

import com.neit.hoccode.dto.response.RoleResponse;
import com.neit.hoccode.entity.Role;
import com.neit.hoccode.mapper.RoleMapper;
import com.neit.hoccode.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role addRole(String name){
        return roleRepository.save(Role.builder().name(name).build());
    }
    public Role getRole(int id){
        return roleRepository.getReferenceById(id);
    }
}
