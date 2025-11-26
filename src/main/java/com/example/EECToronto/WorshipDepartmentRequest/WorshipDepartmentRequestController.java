package com.example.EECToronto.WorshipDepartmentRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/worship-department-requests")
@CrossOrigin(origins = {"https://www.geecvancouver.ca", "https://geecvancouver.ca", "http://localhost:3036"})
public class WorshipDepartmentRequestController {
    private final WorshipDepartmentRequestService requestService;

    @Autowired
    public WorshipDepartmentRequestController(WorshipDepartmentRequestService requestService) {
        this.requestService = requestService;
    }

    // Public endpoint - no auth needed
    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody WorshipDepartmentRequest request) {
        try {
            WorshipDepartmentRequest created = requestService.createRequest(request);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    // Admin endpoints moved to separate controller - see WorshipDepartmentRequestAdminController

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

