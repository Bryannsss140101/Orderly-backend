package com.project.ordearly.security.auth.domain;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.ordearly.exceptions.DuplicateResourceException;
import com.project.ordearly.exceptions.UnauthorizedException;
import com.project.ordearly.security.auth.dto.AuthResponseDto;
import com.project.ordearly.security.auth.dto.LoginRequestDto;
import com.project.ordearly.security.auth.dto.RegisterRequestDto;
import com.project.ordearly.security.auth.jwt.JwtService;
import com.project.ordearly.user.domain.Role;
import com.project.ordearly.user.domain.User;
import com.project.ordearly.user.infrastructure.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponseDto register(RegisterRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername()))
            throw new DuplicateResourceException("Username already exists");

        if (userRepository.existsByEmail(requestDto.getEmail()))
            throw new DuplicateResourceException("Email already exists");

        var user = User.builder()
                .role(Role.EMPLOYEE)
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();

        var savedUser = userRepository.save(user);

        return buildAuthResponse(savedUser);
    }

    public AuthResponseDto login(LoginRequestDto requestDto) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsernameOrEmail(),
                            requestDto.getPassword()));

            return buildAuthResponse((User) authentication.getPrincipal());
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new UnauthorizedException("Incorrect credentials");
        }
    }

    private AuthResponseDto buildAuthResponse(User user) {
        var token = jwtService.generateToken(user);

        return AuthResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().toString())
                .accessToken(token)
                .build();
    }
}