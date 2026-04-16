package com.project.ordearly.security.auth.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ordearly.security.auth.domain.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
}