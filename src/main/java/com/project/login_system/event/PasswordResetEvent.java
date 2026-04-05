package com.project.login_system.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PasswordResetEvent {
    private final String email;
    private final String name;
    private final String link;
}