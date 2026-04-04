package com.project.login_system.security.auth.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.login_system.security.auth.domain.PasswordResetToken;
import com.project.login_system.user.domain.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    void deleteByUser(User user);
}