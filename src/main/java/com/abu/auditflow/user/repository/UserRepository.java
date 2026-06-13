package com.abu.auditflow.user.repository;

import com.abu.auditflow.user.entity.User;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {
            
    @EntityGraph(attributePaths = "role")
    Optional<User> findByUsername(String username);
}