package com.project.login_system.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateEmailDto {
    @NotBlank
    @Email
    private String email;
}