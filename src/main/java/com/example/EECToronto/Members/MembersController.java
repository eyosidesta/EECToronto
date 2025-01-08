package com.example.EECToronto.Members;

import org.springframework.beans.factory.annotation.Autowired;
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
}
