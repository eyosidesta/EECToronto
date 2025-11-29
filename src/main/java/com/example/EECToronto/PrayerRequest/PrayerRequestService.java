package com.example.EECToronto.PrayerRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrayerRequestService {
    private final PrayerRequestRepository requestRepository;

    @Autowired
    public PrayerRequestService(PrayerRequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public PrayerRequest createRequest(PrayerRequest request) {
        // Validation
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("Name is required");
        }
        
        if (request.getPhone() == null || request.getPhone().trim().isEmpty()) {
            throw new RuntimeException("Phone is required");
        }
        
        // Phone validation: should be 7-15 digits
        String phone = request.getPhone().trim().replaceAll("[^0-9]", "");
        if (phone.length() < 7 || phone.length() > 15) {
            throw new RuntimeException("Phone number must be between 7 and 15 digits");
        }
        request.setPhone(phone);
        
        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            throw new RuntimeException("Message is required");
        }
        
        // Set request date if not set
        if (request.getRequestDate() == null) {
            request.setRequestDate(new java.util.Date());
        }
        
        return requestRepository.save(request);
    }

    public List<PrayerRequest> getAllRequests() {
        return requestRepository.findAllByOrderByRequestDateDesc();
    }

    public List<PrayerRequest> getUncontactedRequests() {
        return requestRepository.findByContacted(false);
    }

    public PrayerRequest markAsContacted(Long id) {
        PrayerRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        if (request.isContacted()) {
            throw new RuntimeException("Request is already contacted");
        }
        
        request.setContacted(true);
        return requestRepository.save(request);
    }

    public long getTotalCount() {
        return requestRepository.count();
    }

    public long getContactedCount() {
        return requestRepository.countByContacted(true);
    }

    public long getUncontactedCount() {
        return requestRepository.countByContacted(false);
    }

    public PrayerRequest getRequestById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
    }
}

