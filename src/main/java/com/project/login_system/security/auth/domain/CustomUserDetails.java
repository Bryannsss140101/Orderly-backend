package com.project.login_system.security.auth.domain;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {
    public Long getId();
}