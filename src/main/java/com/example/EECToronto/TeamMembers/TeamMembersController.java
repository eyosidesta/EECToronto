package com.example.EECToronto.TeamMembers;

import com.example.EECToronto.DTO.TeamMemberDTO;
import com.example.EECToronto.Members.Members;
import com.example.EECToronto.Teams.Teams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path="api/admin/team_members")
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

    @GetMapping("/get_team_members_by_name/{teamName}")
    public List<Members> getMembersByTeamName(@PathVariable String teamName) {
        return teamMembersService.getMembersByTeamName(teamName);
    }

    @GetMapping("/get_team_members_with_dates_by_name/{teamName}")
    public List<TeamMemberDTO> getTeamMembersWithJoinedDateByTeamName(@PathVariable String teamName) {
        return teamMembersService.getTeamMembersWithJoinedDateByTeamName(teamName);
    }

    @DeleteMapping("/remove_member/team/{team_id}/member/{member_id}")
    public void removeMemberFromTeam(@PathVariable Long team_id, @PathVariable Long member_id) {
        teamMembersService.removeMemberFromTeam(team_id, member_id);
    }

    @PutMapping("/update_joined_date/team/{team_id}/member/{member_id}")
    public void updateTeamMemberJoinedDate(@PathVariable Long team_id, @PathVariable Long member_id, @RequestBody java.util.Map<String, String> request) {
        java.time.LocalDate joinedDate = null;
        if (request.containsKey("joinedDate") && request.get("joinedDate") != null && !request.get("joinedDate").isEmpty()) {
            joinedDate = java.time.LocalDate.parse(request.get("joinedDate"));
        }
        teamMembersService.updateTeamMemberJoinedDate(team_id, member_id, joinedDate);
    }

    @GetMapping("/get_team_member/team/{team_id}/member/{member_id}")
    public TeamMembers getTeamMember(@PathVariable Long team_id, @PathVariable Long member_id) {
        return teamMembersService.getTeamMember(team_id, member_id);
    }
}
