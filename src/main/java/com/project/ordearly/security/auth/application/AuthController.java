package com.project.ordearly.security.auth.application;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ordearly.security.auth.domain.AuthService;
import com.project.ordearly.security.auth.dto.AuthResponseDto;
import com.project.ordearly.security.auth.dto.LoginRequestDto;
import com.project.ordearly.security.auth.dto.RegisterRequestDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signUp(
            @RequestBody @Valid RegisterRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(requestDto));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDto> signIn(
            @RequestBody @Valid LoginRequestDto requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }
}