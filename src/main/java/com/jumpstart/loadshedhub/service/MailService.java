package com.jumpstart.loadshedhub.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Sends transactional email (password resets, etc).
 * When app.mail.enabled=false (the default for local/dev), messages are written to the
 * application log instead of being sent, so the flow can be tested without real SMTP
 * credentials. Set app.mail.enabled=true and configure spring.mail.* to send for real
 * in production.
 */
@Service
@Slf4j
public class MailService {

    private final Optional<JavaMailSender> mailSender;
    private final boolean enabled;
    private final String fromAddress;

    public MailService(Optional<JavaMailSender> mailSender,
                        @Value("${app.mail.enabled:false}") boolean enabled,
                        @Value("${app.mail.from:no-reply@loadshedhub.co.za}") String fromAddress) {
        this.mailSender = mailSender;
        this.enabled = enabled;
        this.fromAddress = fromAddress;
    }

    // Fires once at startup. app.mail.enabled=true with no working SMTP host is the most common
    // cause of "the reset email never arrives" reports: sendPasswordResetEmail() otherwise fails
    // silently from the caller's point of view (forgot-password always returns a generic success
    // message so it can't leak which emails are registered). Surface the misconfiguration loudly
    // in the logs instead.
    @PostConstruct
    void checkConfiguration() {
        if (enabled && mailSender.isEmpty()) {
            log.warn("app.mail.enabled=true but no JavaMailSender bean is available - check " +
                    "spring.mail.host (SMTP_HOST) is set. Password reset emails will not be sent.");
        }
    }

    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        String subject = "Reset your LoadShed Hub password";
        String body = "We received a request to reset your LoadShed Hub password.\n\n"
                + "Click the link below to choose a new password. This link expires in 30 minutes.\n\n"
                + resetLink + "\n\n"
                + "If you didn't request this, you can safely ignore this email.";

        if (enabled && mailSender.isPresent()) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.get().send(message);
        } else {
            log.info("[DEV MODE - email not sent] Password reset link for {}: {}", toEmail, resetLink);
        }
    }
}
