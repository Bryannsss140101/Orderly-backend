package com.project.ordearly.auth.dto;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.Test;
import org.junit.Before;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.project.ordearly.security.auth.dto.RegisterRequestDto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@SpringBootTest
public class RegisterRequestDtoValidationTest {
    private Validator validator;

    @Before
    public void setUp() {
        var localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();

        validator = localValidatorFactoryBean;
    }

    @Test
    public void shouldFailValidation_whenFirstNameAndLastNameIsLessThan2() {
        var dto = createValidDto();

        dto.setFirstName("J");
        dto.setLastName("D");

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    public void shouldFailValidation_whenFirstNameAndLastNameIsGreaterThan100() {
        var dto = createValidDto();

        dto.setFirstName("J".repeat(101));
        dto.setLastName("D".repeat(101));

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    public void shouldFailValidation_whenAddressIsBlank() {
        var dto = createValidDto();

        dto.setAddress("");

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("address")));
    }

    @Test
    public void shouldFailValidation_whenBirthdateIsNull() {
        var dto = createValidDto();

        dto.setBirthdate(null);

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("birthdate")));
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
        var dto = new RegisterRequestDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setAddress("Av Peru 123");
        dto.setBirthdate(LocalDate.of(2000, 1, 1));
        dto.setPhotoUrl("http://photo.com/img.jpg");
        dto.setUsername("johndoe");
        dto.setEmail("john@mail.com");
        dto.setPassword("123456");

        return dto;
    }
}