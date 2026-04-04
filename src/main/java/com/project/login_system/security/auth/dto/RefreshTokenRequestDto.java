package com.project.login_system.security.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequestDto {
    @NotBlank
    @JsonProperty("refresh_token")
    private String refreshToken;
}
