package com.project.login_system.event;

import org.springframework.context.ApplicationEvent;

import com.project.login_system.user.domain.User;

import lombok.Getter;

@Getter
public class UserRegisterEvent extends ApplicationEvent {
    private final User user;

    public UserRegisterEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}