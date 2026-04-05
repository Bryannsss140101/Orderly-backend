package com.project.login_system.security.auth.domain;

import com.project.login_system.event.PasswordResetEvent;
import com.project.login_system.event.UserRegisterEvent;
import com.project.login_system.security.auth.infrastructure.PasswordResetTokenRepository;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.login_system.exceptions.DuplicateResourceException;
import com.project.login_system.exceptions.UnauthorizedException;
import com.project.login_system.security.auth.dto.AuthResponseDto;
import com.project.login_system.security.auth.dto.RefreshTokenRequestDto;
import com.project.login_system.security.auth.dto.SignInRequestDto;
import com.project.login_system.security.auth.dto.SignUpRequestDto;
import com.project.login_system.security.auth.jwt.JwtService;
import com.project.login_system.user.domain.Role;
import com.project.login_system.user.domain.User;
import com.project.login_system.user.infrastructure.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ApplicationEventPublisher applicationEventPublisher;
    
    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Transactional
    public AuthResponseDto register(SignUpRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername()))
            throw new DuplicateResourceException("%s already exists", requestDto.getUsername());

        if (userRepository.existsByEmail(requestDto.getEmail()))
            throw new DuplicateResourceException("%s already exists", requestDto.getEmail());

        var user = User.builder()
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(Role.USER)
                .build();

        var savedUser = userRepository.save(user);

        applicationEventPublisher.publishEvent(
                new UserRegisterEvent(
                        savedUser.getFirstName(),
                        savedUser.getLastName(),
                        savedUser.getUsername(),
                        savedUser.getEmail()));

        return buildAuthResponse(savedUser);
    }

    public AuthResponseDto login(SignInRequestDto requestDto) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getIdentifier(),
                            requestDto.getPassword()));

            var account = (User) authentication.getPrincipal();

            return buildAuthResponse(account);
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new UnauthorizedException("Incorrect credentials");
        }
    }

    @Transactional(readOnly = true)
    public AuthResponseDto refreshToken(RefreshTokenRequestDto requestDto) {
        var refreshToken = requestDto.getRefreshToken();

        if (!jwtService.isTokenValid(refreshToken))
            throw new UnauthorizedException("Invalid or expired refresh token");

        var identifier = jwtService.extractUsername(refreshToken);

        var user = userRepository.findByEmailOrUsername(identifier, identifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return buildAuthResponse(user);
    }

    @Transactional
    public void requestPasswordReset(String email) {
        var userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty())
            return;

        var user = userOptional.get();

        var token = UUID.randomUUID().toString();
        var resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();

        passwordResetTokenRepository.save(resetToken);

        var link = frontendUrl + "/reset-password?token=" + token;

        applicationEventPublisher.publishEvent(
                new PasswordResetEvent(
                        user.getEmail(),
                        user.getFirstName(),
                        link));
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        var resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now()))
            throw new UnauthorizedException("Token expired");

        var user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }

    private AuthResponseDto buildAuthResponse(User user) {
        var token = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }
}