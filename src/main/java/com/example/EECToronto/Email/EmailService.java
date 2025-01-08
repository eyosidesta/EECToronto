package com.example.EECToronto.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmailService {
    private final EmailRepository emailRepository;
    @Autowired
    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }
    public List<EmailModel> getAllBulkEmails() {
        return emailRepository.findAll();
    }
}
