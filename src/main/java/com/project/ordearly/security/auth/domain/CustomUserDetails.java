package com.project.ordearly.security.auth.domain;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {
    public Long getId();
}