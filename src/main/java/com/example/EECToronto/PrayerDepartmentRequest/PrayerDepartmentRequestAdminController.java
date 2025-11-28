package com.example.EECToronto.PrayerDepartmentRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/prayer-department-requests")
@CrossOrigin(origins = {"https://www.geecvancouver.ca", "https://geecvancouver.ca", "http://localhost:3036"})
public class PrayerDepartmentRequestAdminController {
    private final PrayerDepartmentRequestService requestService;

    @Autowired
    public PrayerDepartmentRequestAdminController(PrayerDepartmentRequestService requestService) {
        this.requestService = requestService;
    }

    // Admin endpoints - require auth (handled by SecurityConfig)
    @GetMapping
    public ResponseEntity<List<PrayerDepartmentRequest>> getAllRequests() {
        List<PrayerDepartmentRequest> requests = requestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/uncontacted")
    public ResponseEntity<List<PrayerDepartmentRequest>> getUncontactedRequests() {
        List<PrayerDepartmentRequest> requests = requestService.getUncontactedRequests();
        return ResponseEntity.ok(requests);
    }

    @PatchMapping("/{id}/contacted")
    public ResponseEntity<?> markAsContacted(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body) {
        try {
            PrayerDepartmentRequest updated = requestService.markAsContacted(id);
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

    @PostMapping("/{id}/add-to-members")
    public ResponseEntity<?> addToMembersList(@PathVariable Long id) {
        try {
            String message = requestService.addToMembersList(id);
            return ResponseEntity.ok(message);
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


