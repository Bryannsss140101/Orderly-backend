package com.project.login_system.email.listener;

import java.util.HashMap;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.project.login_system.email.service.EmailService;
import com.project.login_system.event.UserRegisterEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailListener {
    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegister(UserRegisterEvent event) {
        var user = event.getUser();

        var templateModel = new HashMap<String, Object>();
        templateModel.put("firstName", user.getFirstName());
        templateModel.put("lastName", user.getLastName());
        templateModel.put("username", user.getUsername());
        templateModel.put("email", user.getEmail());

        emailService.sendEmail(
                user.getEmail(),
                buildSubject(user.getFirstName()),
                "notification-template",
                templateModel);
    }

    private String buildSubject(String firstName) {
        return String.format("%s, your account has been created successfully", firstName);
    }
}
