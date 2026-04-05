package com.project.login_system.security.auth.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.project.login_system.security.auth.domain.CustomUserDetails;

import lombok.var;

@Component("securityUtils")
public final class SecurityUtils {
    public CustomUserDetails getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            throw new RuntimeException("User not authenticated");

        var principal = authentication.getPrincipal();

        if (!(principal instanceof CustomUserDetails))
            return null;

        return (CustomUserDetails) principal;
    }

    public Long getCurrentUserId() {
        var user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    public String getCurrentUsername() {
        var user = getCurrentUser();
        return user != null ? user.getUsername() : null;
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
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null)
            return false;
        return currentUserId.equals(userId);
    }

    public boolean isCurrentUserOrAdmin(Long userId) {
        return isCurrentUser(userId) || isAdmin();
    }
}