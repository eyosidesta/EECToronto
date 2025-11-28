package com.example.EECToronto.Members;

import com.example.EECToronto.DTO.AddMemberDTO;
import com.example.EECToronto.DTO.AddMemberToDepartmentDTO;
import com.example.EECToronto.TeamMembers.TeamMembers;
import com.example.EECToronto.TeamMembers.TeamMembersRepository;
import com.example.EECToronto.TeamMembers.TeamMembersService;
import com.example.EECToronto.Teams.Teams;
import com.example.EECToronto.Teams.TeamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api/admin/members")
public class MembersController {
    private final MembersService membersService;
    private final TeamMembersService teamMembersService;
    private final MembersRepository membersRepository;
    private final TeamsRepository teamsRepository;
    private final TeamMembersRepository teamMembersRepository;

    @Autowired
    public MembersController(MembersService membersService, TeamMembersService teamMembersService, 
                             MembersRepository membersRepository, TeamsRepository teamsRepository,
                             TeamMembersRepository teamMembersRepository) {
        this.membersService = membersService;
        this.teamMembersService = teamMembersService;
        this.membersRepository = membersRepository;
        this.teamsRepository = teamsRepository;
        this.teamMembersRepository = teamMembersRepository;
    }

    @GetMapping
    public List<Members> getAllMembers() {
        return membersService.getAllMembersService();
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateMember(@PathVariable Long id, @RequestBody Members member) {
        try {
            Members updated = membersService.updateMemberService(id, member);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Members> getMemberById(@PathVariable Long id) {
        Members member = membersService.getMemberById(id);
        return ResponseEntity.ok(member);
    }

    @GetMapping("/{id}/teams")
    public ResponseEntity<List<Teams>> getMemberTeams(@PathVariable Long id) {
        List<Teams> teams = teamMembersService.getTeamsByMembersService(id);
        return ResponseEntity.ok(teams);
    }

    @PostMapping("/add-to-department/{departmentName}")
    public ResponseEntity<?> addMemberToDepartment(@PathVariable String departmentName, @RequestBody AddMemberToDepartmentDTO memberRequest) {
        try {
            // 1. Check if member exists by phone or email
            Optional<Members> existingMemberByPhone = membersRepository.findMemberByPhone(memberRequest.getPhone());
            Optional<Members> existingMemberByEmail = Optional.empty();
            
            if (memberRequest.getEmail() != null && !memberRequest.getEmail().trim().isEmpty()) {
                existingMemberByEmail = membersRepository.findMemberByEmail(memberRequest.getEmail());
            }

            Members member;
            if (existingMemberByPhone.isPresent()) {
                member = existingMemberByPhone.get();
            } else if (existingMemberByEmail.isPresent()) {
                member = existingMemberByEmail.get();
            } else {
                // 2. Create new member if doesn't exist
                member = new Members();
                member.setName(memberRequest.getName());
                member.setGender(memberRequest.getGender());
                member.setPhone(memberRequest.getPhone());
                member.setEmail(memberRequest.getEmail()); // Can be null
                membersRepository.save(member);
            }

            // 3. Find department team
            Teams departmentTeam = teamsRepository.findByTeam_nameIgnoreCase(departmentName)
                    .or(() -> {
                        // Try without "Department" suffix
                        String teamNameWithoutSuffix = departmentName.replace(" Department", "").replace(" department", "");
                        return teamsRepository.findByTeam_nameIgnoreCase(teamNameWithoutSuffix);
                    })
                    .orElseThrow(() -> new RuntimeException(departmentName + " team not found. Please create it first."));

            // 4. Check if member is already in the team
            if (teamMembersRepository.existsByTeamsAndMembers(departmentTeam, member)) {
                return ResponseEntity.badRequest().body(new ErrorResponse("This member is already in the " + departmentName + " list."));
            }

            // 5. Add to TeamMembers with joinedDate (use provided date or today)
            java.time.LocalDate joinedDate = memberRequest.getJoinedDate() != null 
                ? memberRequest.getJoinedDate() 
                : java.time.LocalDate.now();
            TeamMembers teamMember = new TeamMembers(departmentTeam, member, joinedDate);
            teamMembersRepository.save(teamMember);

            return ResponseEntity.ok(new SuccessResponse("Member successfully added to " + departmentName + "."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Failed to add member: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> addMember(@RequestBody AddMemberDTO memberRequest) {
        try {
            // Check if member exists by phone or email
            Optional<Members> existingMemberByPhone = membersRepository.findMemberByPhone(memberRequest.getPhone());
            Optional<Members> existingMemberByEmail = Optional.empty();
            
            if (memberRequest.getEmail() != null && !memberRequest.getEmail().trim().isEmpty()) {
                existingMemberByEmail = membersRepository.findMemberByEmail(memberRequest.getEmail());
            }

            if (existingMemberByPhone.isPresent() || existingMemberByEmail.isPresent()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Member with this phone or email already exists."));
            }

            // Create new member
            Members member = new Members();
            member.setName(memberRequest.getName());
            member.setGender(memberRequest.getGender());
            member.setPhone(memberRequest.getPhone());
            member.setEmail(memberRequest.getEmail());
            if (memberRequest.getDateOfBirth() != null) {
                member.setDate_of_birth(java.sql.Date.valueOf(memberRequest.getDateOfBirth()));
            }
            member.setAddress(memberRequest.getAddress());
            
            membersService.addMemberService(member);
            return ResponseEntity.ok(new SuccessResponse("Member created successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Failed to create member: " + e.getMessage()));
        }
    }

    private static class ErrorResponse {
        private String message;
        public ErrorResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
    }

    private static class SuccessResponse {
        private String message;
        public SuccessResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
    }
}
