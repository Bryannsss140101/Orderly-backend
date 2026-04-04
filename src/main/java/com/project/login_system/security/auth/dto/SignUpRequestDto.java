package com.project.login_system.security.auth.dto;

import java.time.LocalDate;

import com.project.login_system.user.domain.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequestDto {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @NotBlank
    @Pattern(regexp = "^\\+?\\d{9,15}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotNull
    @Past
    private LocalDate birthday;

    @NotNull
    private Gender gender;
}