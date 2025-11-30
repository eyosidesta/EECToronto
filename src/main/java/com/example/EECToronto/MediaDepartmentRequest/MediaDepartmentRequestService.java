package com.example.EECToronto.MediaDepartmentRequest;

import com.example.EECToronto.Members.Members;
import com.example.EECToronto.Members.MembersRepository;
import com.example.EECToronto.TeamMembers.TeamMembers;
import com.example.EECToronto.TeamMembers.TeamMembersRepository;
import com.example.EECToronto.Teams.Teams;
import com.example.EECToronto.Teams.TeamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MediaDepartmentRequestService {
    private final MediaDepartmentRequestRepository requestRepository;
    private final MembersRepository membersRepository;
    private final TeamsRepository teamsRepository;
    private final TeamMembersRepository teamMembersRepository;

    @Autowired
    public MediaDepartmentRequestService(
            MediaDepartmentRequestRepository requestRepository,
            MembersRepository membersRepository,
            TeamsRepository teamsRepository,
            TeamMembersRepository teamMembersRepository) {
        this.requestRepository = requestRepository;
        this.membersRepository = membersRepository;
        this.teamsRepository = teamsRepository;
        this.teamMembersRepository = teamMembersRepository;
    }

    public MediaDepartmentRequest createRequest(MediaDepartmentRequest request) {
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
        
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        
        // Email validation
        String email = request.getEmail().trim().toLowerCase();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new RuntimeException("Invalid email format");
        }
        request.setEmail(email);
        
        // Capitalize gender (Male/Female)
        if (request.getGender() != null && !request.getGender().trim().isEmpty()) {
            String gender = request.getGender().trim().toLowerCase();
            if (gender.equals("male") || gender.equals("female")) {
                request.setGender(gender.substring(0, 1).toUpperCase() + gender.substring(1));
            }
        }
        
        // Check for duplicate pending requests (same phone or email, not contacted)
        Optional<MediaDepartmentRequest> existingByPhone = requestRepository.findByPhoneAndContacted(phone, false);
        Optional<MediaDepartmentRequest> existingByEmail = requestRepository.findByEmailAndContacted(email, false);
        
        boolean duplicateExists = (existingByPhone.isPresent() && (request.getId() == null || 
                !existingByPhone.get().getId().equals(request.getId()))) ||
                (existingByEmail.isPresent() && (request.getId() == null || 
                !existingByEmail.get().getId().equals(request.getId())));
        
        if (duplicateExists) {
            throw new RuntimeException("You already have a pending request. Please wait for our team to contact you.");
        }
        
        // Set request date if not set
        if (request.getRequestDate() == null) {
            request.setRequestDate(new java.util.Date());
        }
        
        return requestRepository.save(request);
    }

    public List<MediaDepartmentRequest> getAllRequests() {
        return requestRepository.findAllByOrderByRequestDateDesc();
    }

    public List<MediaDepartmentRequest> getUncontactedRequests() {
        return requestRepository.findByContacted(false);
    }

    public MediaDepartmentRequest markAsContacted(Long id) {
        MediaDepartmentRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setContacted(true);
        return requestRepository.save(request);
    }

    public void deleteRequest(Long id) {
        if (!requestRepository.existsById(id)) {
            throw new RuntimeException("Request not found");
        }
        requestRepository.deleteById(id);
    }

    public String addToMembersList(Long requestId) {
        // 1. Get request
        MediaDepartmentRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        // 2. Check if member exists by phone or email
        Optional<Members> existingMemberByPhone = membersRepository.findMemberByPhone(request.getPhone());
        Optional<Members> existingMemberByEmail = Optional.empty();
        
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            existingMemberByEmail = membersRepository.findMemberByEmail(request.getEmail());
        }

        Members member;
        if (existingMemberByPhone.isPresent()) {
            member = existingMemberByPhone.get();
        } else if (existingMemberByEmail.isPresent()) {
            member = existingMemberByEmail.get();
        } else {
            // 3. Create new member
            member = new Members();
            member.setName(request.getName());
            member.setGender(request.getGender());
            member.setPhone(request.getPhone());
            member.setEmail(request.getEmail());
            // date_of_birth and address can be null
            membersRepository.save(member);
        }

        // 4. Find Media Department team (should already exist)
        Teams mediaTeam = teamsRepository.findByTeam_nameIgnoreCase("Media Department")
                .or(() -> teamsRepository.findByTeam_nameIgnoreCase("Media"))
                .orElseThrow(() -> new RuntimeException("Media Department team not found. Please create it first."));

        // 5. Check if member is already in the team
        if (!teamMembersRepository.existsByTeamsAndMembers(mediaTeam, member)) {
            // 6. Add to TeamMembers with joinedDate from requestDate or today
            java.time.LocalDate joinedDate;
            if (request.getRequestDate() != null) {
                joinedDate = request.getRequestDate().toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();
            } else {
                joinedDate = java.time.LocalDate.now();
            }
            TeamMembers teamMember = new TeamMembers(mediaTeam, member, joinedDate);
            teamMembersRepository.save(teamMember);
        }

        // 7. Delete request
        requestRepository.delete(request);

        return "Member added to Media Department successfully";
    }
}


