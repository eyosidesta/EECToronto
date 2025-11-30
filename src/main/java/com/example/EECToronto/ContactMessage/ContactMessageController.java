package com.example.EECToronto.ContactMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact-messages")
@CrossOrigin(origins = {"https://www.geecvancouver.ca", "https://geecvancouver.ca", "https://geecvancouver.vercel.app", "http://localhost:3036"})
public class ContactMessageController {
    private final ContactMessageService messageService;

    @Autowired
    public ContactMessageController(ContactMessageService messageService) {
        this.messageService = messageService;
    }

    // Public endpoint - no auth needed
    @PostMapping
    public ResponseEntity<?> createMessage(@RequestBody ContactMessage message) {
        try {
            ContactMessage created = messageService.createMessage(message);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    // Helper class for error responses
    private static class ErrorResponse {
        private String message;
        
        public ErrorResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
    }
}


