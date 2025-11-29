package com.example.EECToronto.AcceptChristRequest;

import com.example.EECToronto.Members.Members;
import com.example.EECToronto.Members.MembersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AcceptChristRequestService {
    private final AcceptChristRequestRepository requestRepository;
    private final MembersRepository membersRepository;

    @Autowired
    public AcceptChristRequestService(
            AcceptChristRequestRepository requestRepository,
            MembersRepository membersRepository) {
        this.requestRepository = requestRepository;
        this.membersRepository = membersRepository;
    }

    public AcceptChristRequest createRequest(AcceptChristRequest request) {
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
        
        // Email is optional, but if provided, validate it
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            String email = request.getEmail().trim().toLowerCase();
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new RuntimeException("Invalid email format");
            }
            request.setEmail(email);
        } else {
            request.setEmail(null);
        }
        
        // SaidYesFor is required
        if (request.getSaidYesFor() == null || request.getSaidYesFor().trim().isEmpty()) {
            throw new RuntimeException("Please select what you said yes to today");
        }
        
        // Check for duplicate pending requests (same phone, not contacted)
        Optional<AcceptChristRequest> existingByPhone = requestRepository.findByPhoneAndContacted(phone, false);
        
        boolean duplicateExists = existingByPhone.isPresent() && (request.getId() == null || 
                !existingByPhone.get().getId().equals(request.getId()));
        
        if (duplicateExists) {
            throw new RuntimeException("You already have a pending request. Please wait for our team to contact you.");
        }
        
        // Set request date if not set
        if (request.getRequestDate() == null) {
            request.setRequestDate(new java.util.Date());
        }
        
        // Status is null initially (will be set when marked as contacted)
        request.setStatus(null);
        
        return requestRepository.save(request);
    }

    public List<AcceptChristRequest> getAllRequests() {
        return requestRepository.findAllByOrderByRequestDateDesc();
    }

    public List<AcceptChristRequest> getUncontactedRequests() {
        return requestRepository.findByContacted(false);
    }

    public List<AcceptChristRequest> getContactedRequests() {
        return requestRepository.findByContacted(true);
    }

    public AcceptChristRequest markAsContacted(Long id) {
        AcceptChristRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        // Set contacted = true and default status
        request.setContacted(true);
        request.setStatus("Contacted to accept Christ");
        
        // Does NOT add to members list (only moves to contacted tab)
        return requestRepository.save(request);
    }

    public AcceptChristRequest updateContactedRequest(Long id, String name, String phone, String email, String status) {
        AcceptChristRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        // Update request fields
        if (name != null && !name.trim().isEmpty()) {
            request.setName(name.trim());
        }
        
        if (phone != null && !phone.trim().isEmpty()) {
            // Clean phone number
            String cleanedPhone = phone.trim().replaceAll("[^0-9]", "");
            if (cleanedPhone.length() >= 7 && cleanedPhone.length() <= 15) {
                request.setPhone(cleanedPhone);
            } else {
                throw new RuntimeException("Phone number must be between 7 and 15 digits");
            }
        }
        
        // Email is optional
        if (email != null && !email.trim().isEmpty()) {
            String emailLower = email.trim().toLowerCase();
            if (emailLower.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                request.setEmail(emailLower);
            } else {
                throw new RuntimeException("Invalid email format");
            }
        } else {
            request.setEmail(null);
        }
        
        // Status can be updated
        if (status != null && !status.trim().isEmpty()) {
            request.setStatus(status.trim());
        }
        
        // Also update the corresponding member in the members list if they exist
        Optional<Members> existingMemberByPhone = membersRepository.findMemberByPhone(request.getPhone());
        if (existingMemberByPhone.isPresent()) {
            Members member = existingMemberByPhone.get();
            member.setName(request.getName());
            member.setPhone(request.getPhone());
            member.setEmail(request.getEmail());
            membersRepository.save(member);
        }
        
        return requestRepository.save(request);
    }

    public String addToMembersList(Long requestId) {
        // Get request
        AcceptChristRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        // Check if member exists by phone or email
        Optional<Members> existingMemberByPhone = membersRepository.findMemberByPhone(request.getPhone());
        Optional<Members> existingMemberByEmail = Optional.empty();
        
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            existingMemberByEmail = membersRepository.findMemberByEmail(request.getEmail());
        }

        Members member;
        if (existingMemberByPhone.isPresent()) {
            member = existingMemberByPhone.get();
            // Update member info if needed
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty() && 
                (member.getEmail() == null || member.getEmail().trim().isEmpty())) {
                member.setEmail(request.getEmail());
            }
            membersRepository.save(member);
        } else if (existingMemberByEmail.isPresent()) {
            member = existingMemberByEmail.get();
            // Update member info if needed
            if (member.getPhone() == null || member.getPhone().trim().isEmpty()) {
                member.setPhone(request.getPhone());
            }
            membersRepository.save(member);
        } else {
            // Create new member
            member = new Members();
            member.setName(request.getName());
            member.setPhone(request.getPhone());
            member.setEmail(request.getEmail()); // Can be null
            // date_of_birth and address can be null
            membersRepository.save(member);
        }

        return "Member added to members list successfully";
    }
}

