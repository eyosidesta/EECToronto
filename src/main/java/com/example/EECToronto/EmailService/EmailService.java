package com.example.EECToronto.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${app.frontend.url:https://www.geecvancouver.ca}")
    private String frontendUrl;
    
    public void sendRegistrationNotification(String toEmail, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Access Granted - Grace Ethiopian Evangelical Church");
            
            String loginUrl = frontendUrl + "/login";
            String emailBody = "Admin from Grace Ethiopian Evangelical Church gave you access to the website.\n\n" +
                    "Username: " + username + "\n\n" +
                    "Click the following link to get redirected to the login page:\n" +
                    loginUrl + "\n\n" +
                    "Please use your email address as your username to log in.";
            
            message.setText(emailBody);
            
            mailSender.send(message);
        } catch (Exception e) {
            // Log error but don't throw - registration should still succeed even if email fails
            System.err.println("Failed to send registration email: " + e.getMessage());
        }
    }
    
    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Password Reset - Grace Ethiopian Evangelical Church");
            
            String resetUrl = frontendUrl + "/reset-password?token=" + resetToken;
            String emailBody = "You requested a password reset for your account.\n\n" +
                    "Click the following link to reset your password:\n" +
                    resetUrl + "\n\n" +
                    "This link will expire in 1 hour.\n\n" +
                    "If you did not request this, please ignore this email.";
            
            message.setText(emailBody);
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send password reset email: " + e.getMessage());
            throw new RuntimeException("Failed to send password reset email");
        }
    }
}

