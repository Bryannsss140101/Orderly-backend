package com.project.login_system.email.listener;

import java.util.HashMap;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.project.login_system.email.service.EmailService;
import com.project.login_system.event.PasswordResetEvent;
import com.project.login_system.event.UserRegisterEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailListener {
    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegister(UserRegisterEvent event) {
        var templateModel = new HashMap<String, Object>();
        templateModel.put("firstName", event.getFirstName());
        templateModel.put("lastName", event.getLastName());
        templateModel.put("username", event.getUsername());
        templateModel.put("email", event.getEmail());

        emailService.sendEmail(
                event.getEmail(),
                buildSubject(event.getFirstName()),
                "notification-template",
                templateModel);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePasswordReset(PasswordResetEvent event) {
        emailService.sendPasswordResetEmail(
                event.getEmail(),
                event.getName(),
                event.getLink());
    }

    private String buildSubject(String firstName) {
        return String.format("%s, your account has been created successfully", firstName);
    }
}
