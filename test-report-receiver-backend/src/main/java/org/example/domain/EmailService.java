package org.example.domain;

import org.example.data.AppUserRepository;
import org.example.models.AppUser;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    private final AppUserRepository appUserRepository;

    private final String SUBJECT = "Set up your Dev10 Application Account";

    private final String FROM;

    private final String FRONTEND_URL;

    public EmailService(JavaMailSender javaMailSender, AppUserRepository appUserRepository) {
        this.javaMailSender = javaMailSender;
        this.appUserRepository = appUserRepository;
        this.FROM = loadRequiredEnvVar("EMAIL_USERNAME");
        this.FRONTEND_URL = loadRequiredEnvVar("FRONTEND_URL");
    }

    public void sendSetupEmailsToUsers(List<Integer> ids) {
        List<AppUser> users = appUserRepository.findWhereIdInList(ids);
        for (AppUser user : users) {
            sendEmail(user.getUsername(), SUBJECT, buildBody(user.getAccountSetupToken()));
        }
    }

    private String buildBody(String setupToken) {
        return String.format("Click this link to set up your account: %s/setupAccount/%s", FRONTEND_URL, setupToken);
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        mailMessage.setFrom(FROM);
        javaMailSender.send(mailMessage);
    }

    private class EnvironmentVariableNotFoundException extends RuntimeException {
        private EnvironmentVariableNotFoundException(String message) {
            super(message);
        }
    }

    private String loadRequiredEnvVar(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            throw new EnvironmentVariableNotFoundException(String.format("Environment variable %s not found", key));
        }
        return value;
    }
}
