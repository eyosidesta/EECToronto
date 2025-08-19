package com.example.EECToronto.TeamMembers;

import com.example.EECToronto.Members.Members;
import com.example.EECToronto.Teams.Teams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path="api/team_members")
public class TeamMembersController {
    private final TeamMembersService teamMembersService;
    @Autowired
    public TeamMembersController(TeamMembersService teamMembersService) {
        this.teamMembersService = teamMembersService;
    }

    @GetMapping("/get_all_members")
    public List<TeamMembers> getAllTeamMembers() {
        return teamMembersService.getAllTeamMembersService();
    }

    @GetMapping("/get_member_teams/member/{member_id}")
    public List<Teams> getTeamsByMembers(@PathVariable Long member_id) {
         return teamMembersService.getTeamsByMembersService(member_id);
    }

    @GetMapping("/get_team_members/team/{team_id}")
    public List<Members> getMembersByTeamssService(@PathVariable Long team_id) {
        return teamMembersService.getMembersByTeamsService(team_id);
    }

    @PostMapping("/add_member/team/{team_id}/member/{member_id}")
    public void addMemberToTeam(@PathVariable Long team_id, @PathVariable Long member_id) {
        System.out.println("Yes we do*******: " + team_id);
        teamMembersService.addMemberToTeam(team_id, member_id);
    }
}
