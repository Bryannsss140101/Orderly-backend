package com.project.ordearly.auth.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ordearly.exceptions.DuplicateResourceException;
import com.project.ordearly.exceptions.UnauthorizedException;
import com.project.ordearly.global.GlobalExceptionHandler;
import com.project.ordearly.security.auth.application.AuthController;
import com.project.ordearly.security.auth.domain.AuthService;
import com.project.ordearly.security.auth.dto.AuthResponseDto;
import com.project.ordearly.security.auth.dto.LoginRequestDto;
import com.project.ordearly.security.auth.dto.RegisterRequestDto;
import com.project.ordearly.security.auth.jwt.JwtService;
import com.project.ordearly.user.domain.UserService;

@WebMvcTest(AuthController.class)
@Import(GlobalExceptionHandler.class)
@ActiveProfiles("test")
public class AuthControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private AuthService authService;

        @MockBean
        private JwtService jwtService;

        @MockBean
        private UserService userService;

        private RegisterRequestDto validRegisterRequestDto;
        private LoginRequestDto validLoginRequestDto;
        private AuthResponseDto validAuthResponseDto;

        @BeforeEach
        public void setUp() {
                validRegisterRequestDto = RegisterRequestDto.builder()
                                .username("johndoe")
                                .email("john@mail.com")
                                .password("123456")
                                .build();

                validLoginRequestDto = LoginRequestDto.builder()
                                .usernameOrEmail("johndoe")
                                .password("123456")
                                .build();

                validAuthResponseDto = AuthResponseDto.builder()
                                .id(1L)
                                .username("johndoe")
                                .role("EMPLOYEE")
                                .accessToken("access-jwt")
                                .build();
        }

        @Test
        @WithMockUser
        public void shouldReturn201_whenRegisterRequestDtoIsValid() throws Exception {
                when(authService.register(any(RegisterRequestDto.class)))
                                .thenReturn(validAuthResponseDto);

                mockMvc.perform(post("/api/v1/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validRegisterRequestDto))
                                .with(csrf()))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.username").value("johndoe"))
                                .andExpect(jsonPath("$.role").value("EMPLOYEE"))
                                .andExpect(jsonPath("$.access_token").value("access-jwt"))
                                .andExpect(jsonPath("$.refresh_token").value("refresh-jwt"));

                verify(authService).register(any(RegisterRequestDto.class));
        }

        @Test
        @WithMockUser
        public void shouldReturn401_whenRegisterRequestDtoIsInalid() throws Exception {
                validRegisterRequestDto.setEmail("invalid-email");

                mockMvc.perform(post("/api/v1/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validRegisterRequestDto))
                                .with(csrf()))
                                .andExpect(status().isBadRequest());

                verify(authService, never()).register(any());
        }

        @Test
        @WithMockUser
        public void shouldReturn409_whenRegisterRequestDtoIsInalid() throws Exception {
                when(authService.register(any(RegisterRequestDto.class)))
                                .thenThrow(new DuplicateResourceException("Email already exists"));

                mockMvc.perform(post("/api/v1/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validRegisterRequestDto))
                                .with(csrf()))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.status").value(409))
                                .andExpect(jsonPath("$.error").value("Conflict"))
                                .andExpect(jsonPath("$.message").value("Email already exists"))
                                .andExpect(jsonPath("$.path").value("/api/v1/auth/signup"))
                                .andExpect(jsonPath("$.timestamp").exists());

                verify(authService).register(any(RegisterRequestDto.class));
        }

        @Test
        @WithMockUser
        void shouldReturn200_whenLoginRequestDtoIsValid() throws Exception {
                when(authService.login(any(LoginRequestDto.class)))
                                .thenReturn(validAuthResponseDto);

                mockMvc.perform(post("/api/v1/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validLoginRequestDto))
                                .with(csrf()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.username").value("johndoe"))
                                .andExpect(jsonPath("$.role").value("EMPLOYEE"))
                                .andExpect(jsonPath("$.access_token").value("access-jwt"))
                                .andExpect(jsonPath("$.refresh_token").value("refresh-jwt"));

                verify(authService).login(any(LoginRequestDto.class));
        }

        @Test
        @WithMockUser
        void shouldReturn400_whenLoginRequestDtoIsInvalid() throws Exception {
                validLoginRequestDto.setUsernameOrEmail("");

                mockMvc.perform(post("/api/v1/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validLoginRequestDto))
                                .with(csrf()))
                                .andExpect(status().isBadRequest());

                verify(authService, never()).login(any());
        }

        @Test
        @WithMockUser
        void shouldReturn401_whenCredentialsAreInvalid() throws Exception {
                when(authService.login(any(LoginRequestDto.class)))
                                .thenThrow(new UnauthorizedException("Incorrect credentials"));

                mockMvc.perform(post("/api/v1/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validLoginRequestDto))
                                .with(csrf()))
                                .andExpect(status().isUnauthorized())
                                .andExpect(jsonPath("$.status").value(401))
                                .andExpect(jsonPath("$.error").value("Unauthorized"))
                                .andExpect(jsonPath("$.message").value("Incorrect credentials"))
                                .andExpect(jsonPath("$.path").value("/api/v1/auth/signin"))
                                .andExpect(jsonPath("$.timestamp").exists());

                verify(authService).login(any(LoginRequestDto.class));
        }
}