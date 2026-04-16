package com.project.ordearly.user.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ordearly.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}