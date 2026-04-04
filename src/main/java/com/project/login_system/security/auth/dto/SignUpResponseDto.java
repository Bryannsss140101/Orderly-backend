package com.project.login_system.security.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpResponseDto {
    private String accessToken;
    private String refreshToken;
}