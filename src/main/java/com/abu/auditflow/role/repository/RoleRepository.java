package com.abu.auditflow.role.repository;

import com.abu.auditflow.role.entity.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository
        extends JpaRepository<Role, Long> {
       Optional<Role> findByCode(String code);
}