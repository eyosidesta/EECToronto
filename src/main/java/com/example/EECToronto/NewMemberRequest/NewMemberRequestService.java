package com.example.EECToronto.NewMemberRequest;

import com.example.EECToronto.Members.Members;
import com.example.EECToronto.Members.MembersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewMemberRequestService {
    private final NewMemberRequestRepository requestRepository;
    private final MembersRepository membersRepository;

    @Autowired
    public NewMemberRequestService(
            NewMemberRequestRepository requestRepository,
            MembersRepository membersRepository) {
        this.requestRepository = requestRepository;
        this.membersRepository = membersRepository;
    }

    public NewMemberRequest createRequest(NewMemberRequest request) {
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
        request.setPhone(phone); // Store cleaned phone number
        
        // Email is optional, but if provided, validate it
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            String email = request.getEmail().trim().toLowerCase();
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new RuntimeException("Invalid email format");
            }
            request.setEmail(email);
        } else {
            request.setEmail(null); // Explicitly set to null if empty
        }
        
        // Capitalize gender (Male/Female)
        if (request.getGender() != null && !request.getGender().trim().isEmpty()) {
            String gender = request.getGender().trim().toLowerCase();
            if (gender.equals("male") || gender.equals("female")) {
                request.setGender(gender.substring(0, 1).toUpperCase() + gender.substring(1));
            }
        }
        
        // Check for duplicate pending requests (same phone, not contacted)
        // Only check by phone since email is optional
        Optional<NewMemberRequest> existingByPhone = requestRepository.findByPhoneAndContacted(phone, false);
        
        boolean duplicateExists = existingByPhone.isPresent() && (request.getId() == null || 
                !existingByPhone.get().getId().equals(request.getId()));
        
        if (duplicateExists) {
            throw new RuntimeException("You already have a pending request. Please wait for our team to contact you.");
        }
        
        // Set request date if not set
        if (request.getRequestDate() == null) {
            request.setRequestDate(new java.util.Date());
        }
        
        return requestRepository.save(request);
    }

    public List<NewMemberRequest> getAllRequests() {
        return requestRepository.findAllByOrderByRequestDateDesc();
    }

    public List<NewMemberRequest> getUncontactedRequests() {
        return requestRepository.findByContacted(false);
    }

    public List<NewMemberRequest> getContactedRequests() {
        return requestRepository.findByContacted(true);
    }

    public NewMemberRequest markAsContacted(Long id) {
        NewMemberRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        // Automatically add to members list
        addToMembersList(request);
        
        // Set contacted = true
        request.setContacted(true);
        return requestRepository.save(request);
    }

    public NewMemberRequest removeFromContacted(Long id) {
        NewMemberRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        // Set contacted = false (but member stays in members list)
        request.setContacted(false);
        return requestRepository.save(request);
    }

    public void deleteRequest(Long id) {
        if (!requestRepository.existsById(id)) {
            throw new RuntimeException("Request not found");
        }
        requestRepository.deleteById(id);
    }

    public NewMemberRequest updateContactedMember(Long id, String name, String phone, String email, String gender) {
        NewMemberRequest request = requestRepository.findById(id)
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
        
        if (gender != null && !gender.trim().isEmpty()) {
            String genderLower = gender.trim().toLowerCase();
            if (genderLower.equals("male") || genderLower.equals("female")) {
                request.setGender(genderLower.substring(0, 1).toUpperCase() + genderLower.substring(1));
            }
        }
        
        // Also update the corresponding member in the members list
        Optional<Members> existingMemberByPhone = membersRepository.findMemberByPhone(request.getPhone());
        if (existingMemberByPhone.isPresent()) {
            Members member = existingMemberByPhone.get();
            member.setName(request.getName());
            member.setPhone(request.getPhone());
            member.setEmail(request.getEmail());
            member.setGender(request.getGender());
            membersRepository.save(member);
        }
        
        return requestRepository.save(request);
    }

    private void addToMembersList(NewMemberRequest request) {
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
            if (request.getGender() != null && 
                (member.getGender() == null || member.getGender().trim().isEmpty())) {
                member.setGender(request.getGender());
            }
            membersRepository.save(member);
        } else if (existingMemberByEmail.isPresent()) {
            member = existingMemberByEmail.get();
            // Update member info if needed
            if (member.getPhone() == null || member.getPhone().trim().isEmpty()) {
                member.setPhone(request.getPhone());
            }
            if (request.getGender() != null && 
                (member.getGender() == null || member.getGender().trim().isEmpty())) {
                member.setGender(request.getGender());
            }
            membersRepository.save(member);
        } else {
            // Create new member
            member = new Members();
            member.setName(request.getName());
            member.setGender(request.getGender());
            member.setPhone(request.getPhone());
            member.setEmail(request.getEmail()); // Can be null
            // date_of_birth and address can be null
            membersRepository.save(member);
        }
    }
}

