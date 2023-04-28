package com.tiklup.springsecurity.demo.repository.Security;

import com.tiklup.springsecurity.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    public Role findByNameContainsAllIgnoreCase(String roleName);
}
