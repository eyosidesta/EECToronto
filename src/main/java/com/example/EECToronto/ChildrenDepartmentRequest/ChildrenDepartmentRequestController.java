package com.example.EECToronto.ChildrenDepartmentRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/children-department-requests")
@CrossOrigin(origins = {"https://www.geecvancouver.ca", "https://geecvancouver.ca", "https://geecvancouver.vercel.app", "http://localhost:3036"})
public class ChildrenDepartmentRequestController {
    private final ChildrenDepartmentRequestService requestService;

    @Autowired
    public ChildrenDepartmentRequestController(ChildrenDepartmentRequestService requestService) {
        this.requestService = requestService;
    }

    // Public endpoint - no auth needed
    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody ChildrenDepartmentRequest request) {
        try {
            ChildrenDepartmentRequest created = requestService.createRequest(request);
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

