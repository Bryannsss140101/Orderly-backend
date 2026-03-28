package com.project.login_system.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUsernameDto {
    @NotBlank
    private String username;
}
