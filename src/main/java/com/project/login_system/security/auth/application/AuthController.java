package com.project.login_system.security.auth.application;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.login_system.security.auth.domain.AuthService;
import com.project.login_system.security.auth.dto.AuthResponseDto;
import com.project.login_system.security.auth.dto.RefreshTokenRequestDto;
import com.project.login_system.security.auth.dto.ResetPasswordRequestDto;
import com.project.login_system.security.auth.dto.SignInRequestDto;
import com.project.login_system.security.auth.dto.SignUpRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {
        private final AuthService authService;

        public record GoogleSignInRequest(String token) {
        }

        public record ForgotPasswordRequest(String email) {
        }

        public record MessageResponse(String message) {
        }

        @PostMapping("/signup")
        public ResponseEntity<AuthResponseDto> signUp(
                        @Valid @RequestBody SignUpRequestDto requestDto) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(authService.register(requestDto));
        }

        @PostMapping("/signin")
        public ResponseEntity<AuthResponseDto> signIn(
                        @Valid @RequestBody SignInRequestDto requestDto) {
                return ResponseEntity.ok(authService.login(requestDto));
        }

        @PostMapping("/refresh")
        public ResponseEntity<AuthResponseDto> refreshToken(
                        @Valid @RequestBody RefreshTokenRequestDto requestDto) {
                return ResponseEntity.ok(authService.refreshToken(requestDto));
        }

        @PostMapping("/forgot-password")
        public ResponseEntity<MessageResponse> forgotPassword(
                        @RequestBody @Valid ForgotPasswordRequest request) {
                authService.requestPasswordReset(request.email());
                return ResponseEntity.ok(
                                new MessageResponse("Recovery email sent"));
        }

        @PostMapping("/reset-password")
        public ResponseEntity<MessageResponse> resetPassword(
                        @RequestBody @Valid ResetPasswordRequestDto requestDto) {
                authService.resetPassword(
                                requestDto.getToken(),
                                requestDto.getNewPassword());

                return ResponseEntity.ok(
                                new MessageResponse("Password updated successfully"));
        }
}