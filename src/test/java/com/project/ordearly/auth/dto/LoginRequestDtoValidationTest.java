package com.project.ordearly.auth.dto;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.project.ordearly.security.auth.dto.LoginRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

public class LoginRequestDtoValidationTest {
    private Validator validator;

    @Before
    public void setUp() {
        var localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();

        validator = localValidatorFactoryBean;
    }

    @Test
    public void shouldFailValidation_whenUsernameOrEmailIsBlank() {
        var dto = createValidDto();

        dto.setUsernameOrEmail("");

        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("usernameOrEmail")));
    }

    @Test
    public void shouldFailValidation_whenPasswordIsBlank() {
        var dto = createValidDto();

        dto.setPassword("");

        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    private LoginRequestDto createValidDto() {
        var dto = new LoginRequestDto();
        dto.setUsernameOrEmail("john@mail.com");
        dto.setPassword("123456");

        return dto;
    }
}