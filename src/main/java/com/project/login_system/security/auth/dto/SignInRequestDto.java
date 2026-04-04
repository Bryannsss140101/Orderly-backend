package com.project.login_system.security.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequestDto {
    @NotBlank
    private String identifier;

    @NotBlank
    private String password;
}