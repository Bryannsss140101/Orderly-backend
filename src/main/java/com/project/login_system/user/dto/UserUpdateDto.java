package com.project.login_system.user.dto;

import lombok.Data;

@Data
public class UserUpdateDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
}