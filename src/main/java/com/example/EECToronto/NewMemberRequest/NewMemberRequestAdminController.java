package com.example.EECToronto.NewMemberRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/new-member-requests")
@CrossOrigin(origins = {"https://www.geecvancouver.ca", "https://geecvancouver.ca", "https://geecvancouver.vercel.app", "http://localhost:3036"})
public class NewMemberRequestAdminController {
    private final NewMemberRequestService requestService;

    @Autowired
    public NewMemberRequestAdminController(NewMemberRequestService requestService) {
        this.requestService = requestService;
    }

    // Admin endpoints - require auth (handled by SecurityConfig)
    @GetMapping
    public ResponseEntity<List<NewMemberRequest>> getAllRequests() {
        List<NewMemberRequest> requests = requestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/uncontacted")
    public ResponseEntity<List<NewMemberRequest>> getUncontactedRequests() {
        List<NewMemberRequest> requests = requestService.getUncontactedRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/contacted")
    public ResponseEntity<List<NewMemberRequest>> getContactedRequests() {
        List<NewMemberRequest> requests = requestService.getContactedRequests();
        return ResponseEntity.ok(requests);
    }

    @PatchMapping("/{id}/contacted")
    public ResponseEntity<?> markAsContacted(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body) {
        try {
            NewMemberRequest updated = requestService.markAsContacted(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/remove-from-contacted")
    public ResponseEntity<?> removeFromContacted(@PathVariable Long id) {
        try {
            requestService.removeFromContacted(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateContactedMember(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        try {
            NewMemberRequest updated = requestService.updateContactedMember(
                id,
                body.get("name"),
                body.get("phone"),
                body.get("email"),
                body.get("gender")
            );
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable Long id) {
        try {
            requestService.deleteRequest(id);
            return ResponseEntity.noContent().build();
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

