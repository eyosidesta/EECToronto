package com.example.EECToronto.ContactMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactMessageService {
    private final ContactMessageRepository messageRepository;

    @Autowired
    public ContactMessageService(ContactMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public ContactMessage createMessage(ContactMessage message) {
        // Validation
        if (message.getName() == null || message.getName().trim().isEmpty()) {
            throw new RuntimeException("Name is required");
        }
        
        if (message.getPhone() == null || message.getPhone().trim().isEmpty()) {
            throw new RuntimeException("Phone is required");
        }
        
        // Phone validation: should be 7-15 digits
        String phone = message.getPhone().trim().replaceAll("[^0-9]", "");
        if (phone.length() < 7 || phone.length() > 15) {
            throw new RuntimeException("Phone number must be between 7 and 15 digits");
        }
        message.setPhone(phone);
        
        if (message.getSubject() == null || message.getSubject().trim().isEmpty()) {
            throw new RuntimeException("Subject is required");
        }
        
        if (message.getMessage() == null || message.getMessage().trim().isEmpty()) {
            throw new RuntimeException("Message is required");
        }
        
        // Set request date if not set
        if (message.getRequestDate() == null) {
            message.setRequestDate(new java.util.Date());
        }
        
        return messageRepository.save(message);
    }

    public List<ContactMessage> getAllMessages() {
        return messageRepository.findAllByOrderByRequestDateDesc();
    }

    public List<ContactMessage> getUncontactedMessages() {
        return messageRepository.findByContacted(false);
    }

    public ContactMessage markAsContacted(Long id) {
        ContactMessage message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        
        if (message.isContacted()) {
            throw new RuntimeException("Message is already contacted");
        }
        
        message.setContacted(true);
        return messageRepository.save(message);
    }

    public long getTotalCount() {
        return messageRepository.count();
    }

    public long getContactedCount() {
        return messageRepository.countByContacted(true);
    }

    public long getUncontactedCount() {
        return messageRepository.countByContacted(false);
    }

    public ContactMessage getMessageById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }
}

