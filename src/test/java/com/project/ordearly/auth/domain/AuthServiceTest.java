package com.project.ordearly.auth.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.ordearly.exceptions.DuplicateResourceException;
import com.project.ordearly.exceptions.UnauthorizedException;
import com.project.ordearly.security.auth.domain.AuthService;
import com.project.ordearly.security.auth.dto.LoginRequestDto;
import com.project.ordearly.security.auth.dto.RegisterRequestDto;
import com.project.ordearly.security.auth.jwt.JwtService;
import com.project.ordearly.user.domain.Role;
import com.project.ordearly.user.domain.User;
import com.project.ordearly.user.infrastructure.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
        @Mock
        private UserRepository userRepository;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private JwtService jwtService;

        @Mock
        private AuthenticationManager authenticationManager;

        @InjectMocks
        private AuthService authService;

        private User validUser;
        private RegisterRequestDto validRegisterRequestDto;
        private LoginRequestDto validLoginRequestDto;

        @BeforeEach
        public void setUp() {
                validUser = User.builder()
                                .id(1L)
                                .role(Role.EMPLOYEE)
                                .username("johndoe")
                                .email("john@mail.com")
                                .password("123456")
                                .createdAt(ZonedDateTime.parse("2024-01-01T00:00:00Z"))
                                .updatedAt(ZonedDateTime.parse("2024-01-01T00:00:00Z"))
                                .build();

                validRegisterRequestDto = RegisterRequestDto.builder()
                                .username("johndoe")
                                .email("john@mail.com")
                                .password("123456")
                                .build();

                validLoginRequestDto = LoginRequestDto.builder()
                                .usernameOrEmail("johndoe")
                                .password("123456")
                                .build();
        }

        @Test
        public void shouldThrowDuplicateResourceException_whenUsernameAlreadyExists() {
                when(userRepository.existsByUsername(validRegisterRequestDto.getUsername()))
                                .thenReturn(true);

                assertThrows(DuplicateResourceException.class, () -> {
                        authService.register(validRegisterRequestDto);
                });

                verify(userRepository, never()).save(any(User.class));
        }

        @Test
        public void shouldThrowDuplicateResourceException_whenEmailAlreadyExists() {
                when(userRepository.existsByUsername(validRegisterRequestDto.getUsername()))
                                .thenReturn(false);

                when(userRepository.existsByEmail(validRegisterRequestDto.getEmail()))
                                .thenReturn(true);

                assertThrows(DuplicateResourceException.class, () -> {
                        authService.register(validRegisterRequestDto);
                });

                verify(userRepository, never()).save(any(User.class));
        }

        @Test
        public void shouldRegister_whenRegisterRequestDtoIsValid() {
                when(userRepository.existsByUsername(validRegisterRequestDto.getUsername()))
                                .thenReturn(false);

                when(userRepository.existsByEmail(validRegisterRequestDto.getEmail()))
                                .thenReturn(false);

                when(passwordEncoder.encode("123456"))
                                .thenReturn("encodedPassword");

                when(userRepository.save(any(User.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                when(jwtService.generateToken(any(User.class)))
                                .thenReturn("token");

                var response = authService.register(validRegisterRequestDto);

                assertNotNull(response);
                assertNotNull(response.getAccessToken());
                assertEquals(validRegisterRequestDto.getUsername(), response.getUsername());
                assertEquals("EMPLOYEE", response.getRole());

                verify(userRepository).save(any(User.class));
        }

        @Test
        public void shouldLogin_whenLoginRequestDtoIsValid() {
                var authentication = mock(Authentication.class);

                when(authenticationManager.authenticate(any(
                                UsernamePasswordAuthenticationToken.class)))
                                .thenReturn(authentication);

                when(authentication.getPrincipal())
                                .thenReturn(validUser);

                when(jwtService.generateToken(validUser))
                                .thenReturn("token");

                var response = authService.login(validLoginRequestDto);

                assertNotNull(response);
                assertNotNull(response.getAccessToken());
                assertEquals(validRegisterRequestDto.getUsername(), response.getUsername());
                assertEquals("EMPLOYEE", response.getRole());

                verify(authenticationManager).authenticate(any());
        }

        @Test
        void shouldThrowUnauthorizedException_whenLoginRequestDtoIsInvalid() {
                when(authenticationManager.authenticate(any(Authentication.class)))
                                .thenThrow(new BadCredentialsException("bad credentials"));

                assertThrows(UnauthorizedException.class,
                                () -> authService.login(validLoginRequestDto));

                verify(authenticationManager).authenticate(any(Authentication.class));
        }
}