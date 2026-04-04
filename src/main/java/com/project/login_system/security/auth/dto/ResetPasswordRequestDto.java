package com.project.login_system.security.auth.dto;

import lombok.Data;

@Data
public class ResetPasswordRequestDto {
    private String token;
    private String newPassword;
}