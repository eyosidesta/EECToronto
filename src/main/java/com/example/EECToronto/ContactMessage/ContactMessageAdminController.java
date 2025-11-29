package com.example.EECToronto.ContactMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/contact-messages")
@CrossOrigin(origins = {"https://www.geecvancouver.ca", "https://geecvancouver.ca", "https://geecvancouver.vercel.app", "http://localhost:3036"})
public class ContactMessageAdminController {
    private final ContactMessageService messageService;

    @Autowired
    public ContactMessageAdminController(ContactMessageService messageService) {
        this.messageService = messageService;
    }

    // Admin endpoints - require auth (handled by SecurityConfig)
    @GetMapping
    public ResponseEntity<List<ContactMessage>> getAllMessages() {
        List<ContactMessage> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/uncontacted")
    public ResponseEntity<List<ContactMessage>> getUncontactedMessages() {
        List<ContactMessage> messages = messageService.getUncontactedMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Long>> getDashboardMetrics() {
        Map<String, Long> metrics = new HashMap<>();
        metrics.put("total", messageService.getTotalCount());
        metrics.put("contacted", messageService.getContactedCount());
        metrics.put("uncontacted", messageService.getUncontactedCount());
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMessageById(@PathVariable Long id) {
        try {
            ContactMessage message = messageService.getMessageById(id);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/contacted")
    public ResponseEntity<?> markAsContacted(@PathVariable Long id) {
        try {
            ContactMessage updated = messageService.markAsContacted(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    // Helper classes for responses
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

