package com.project.ordearly.auth.dto;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.project.ordearly.security.auth.dto.RegisterRequestDto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

public class RegisterRequestDtoValidationTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        var localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();

        validator = localValidatorFactoryBean;
    }

    @Test
    public void shouldFailValidation_whenUsernameIsBlank() {
        var dto = createValidDto();

        dto.setUsername("");

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @Test
    public void shouldFailValidation_whenUsernameIsLessThan4() {
        var dto = createValidDto();

        dto.setUsername("j");

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @Test
    public void shouldFailValidation_whenUsernameIsGreaterThan30() {
        var dto = createValidDto();

        dto.setUsername("j".repeat(31));

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @Test
    public void shouldFailValidation_whenEmailIsBlank() {
        var dto = createValidDto();

        dto.setEmail("");

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    public void shouldFailValidation_whenEmailIsInvalid() {
        var dto = createValidDto();

        dto.setEmail("invalidemail");

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    public void shouldFailValidation_whenPasswordIsBlank() {
        var dto = createValidDto();

        dto.setPassword("");

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    public void shouldFailValidation_whenPasswordIsLessThan6() {
        var dto = createValidDto();

        dto.setPassword("1");

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    public void shouldFailValidation_whenPasswordIsGreaterThan100() {
        var dto = createValidDto();

        dto.setPassword("1".repeat(101));

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    private RegisterRequestDto createValidDto() {
        return RegisterRequestDto.builder()
                .username("johndoe")
                .email("john@mail.com")
                .password("123456")
                .build();
    }
}