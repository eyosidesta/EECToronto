package com.example.EECToronto.FamilyDepartmentRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/family-department-requests")
@CrossOrigin(origins = {"https://www.geecvancouver.ca", "https://geecvancouver.ca", "http://localhost:3036"})
public class FamilyDepartmentRequestAdminController {
    private final FamilyDepartmentRequestService requestService;

    @Autowired
    public FamilyDepartmentRequestAdminController(FamilyDepartmentRequestService requestService) {
        this.requestService = requestService;
    }

    // Admin endpoints - require auth (handled by SecurityConfig)
    @GetMapping
    public ResponseEntity<List<FamilyDepartmentRequest>> getAllRequests() {
        List<FamilyDepartmentRequest> requests = requestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/uncontacted")
    public ResponseEntity<List<FamilyDepartmentRequest>> getUncontactedRequests() {
        List<FamilyDepartmentRequest> requests = requestService.getUncontactedRequests();
        return ResponseEntity.ok(requests);
    }

    @PatchMapping("/{id}/contacted")
    public ResponseEntity<?> markAsContacted(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body) {
        try {
            FamilyDepartmentRequest updated = requestService.markAsContacted(id);
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



