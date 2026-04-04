package com.project.login_system.security.auth.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.project.login_system.security.auth.domain.CustomUserDetails;

@Component("securityUtils")
public final class SecurityUtils {
    public CustomUserDetails getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            throw new RuntimeException("User not authenticated");

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof CustomUserDetails user))
            throw new RuntimeException("Invalid user principal");

        return user;
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }

    public boolean hasRole(String role) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            return false;

        String authority = "ROLE_" + role.toUpperCase();

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals(authority));
    }

    public boolean isAdmin() {
        return hasRole("ADMIN");
    }

    public boolean isUser() {
        return hasRole("USER");
    }

    public boolean isCurrentUser(Long userId) {
        return userId != null && userId.equals(getCurrentUserId());
    }

    public boolean isCurrentUserOrAdmin(Long userId) {
        return isCurrentUser(userId) || isAdmin();
    }
}