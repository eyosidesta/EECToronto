package com.example.EECToronto.PrayerRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/prayer-requests")
@CrossOrigin(origins = {"https://www.geecvancouver.ca", "https://geecvancouver.ca", "https://geecvancouver.vercel.app", "http://localhost:3036"})
public class PrayerRequestAdminController {
    private final PrayerRequestService requestService;

    @Autowired
    public PrayerRequestAdminController(PrayerRequestService requestService) {
        this.requestService = requestService;
    }

    // Admin endpoints - require auth (handled by SecurityConfig)
    @GetMapping
    public ResponseEntity<List<PrayerRequest>> getAllRequests() {
        List<PrayerRequest> requests = requestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/uncontacted")
    public ResponseEntity<List<PrayerRequest>> getUncontactedRequests() {
        List<PrayerRequest> requests = requestService.getUncontactedRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Long>> getDashboardMetrics() {
        Map<String, Long> metrics = new HashMap<>();
        metrics.put("total", requestService.getTotalCount());
        metrics.put("contacted", requestService.getContactedCount());
        metrics.put("uncontacted", requestService.getUncontactedCount());
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRequestById(@PathVariable Long id) {
        try {
            PrayerRequest request = requestService.getRequestById(id);
            return ResponseEntity.ok(request);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/contacted")
    public ResponseEntity<?> markAsContacted(@PathVariable Long id) {
        try {
            PrayerRequest updated = requestService.markAsContacted(id);
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



