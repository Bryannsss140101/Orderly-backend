package com.project.login_system.email.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.project.login_system.exceptions.EmailSendException;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.from}")
    private String from;

    private void send(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            var context = new Context();
            context.setVariables(variables);

            var htmlContent = templateEngine.process(templateName, context);

            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailSendException("Error sending email", e);
        }
    }

    public void sendEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        send(to, subject, templateName, variables);
    }

    public void sendPasswordResetEmail(String to, String name, String link) {
        Map<String, Object> variables = Map.of(
                "name", name,
                "link", link);

        send(to, "Recover your password", "password-reset-template", variables);
    }
}