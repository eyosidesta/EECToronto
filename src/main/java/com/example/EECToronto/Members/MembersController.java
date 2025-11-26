package com.example.EECToronto.Members;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/members")
public class MembersController {
    private final MembersService membersService;

    @Autowired
    public MembersController(MembersService membersService) {
        this.membersService = membersService;
    }

    @GetMapping
    public List<Members> getAllMembers() {
        return membersService.getAllMembersService();
    }

    @PostMapping
    public void addMember(@RequestBody Members members) {
        membersService.addMemberService(members);
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

    private static class ErrorResponse {
        private String message;
        public ErrorResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
    }
}
