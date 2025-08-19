package com.example.EECToronto.TeamMembers;

import com.example.EECToronto.Members.Members;
import com.example.EECToronto.Members.MembersRepository;
import com.example.EECToronto.Teams.Teams;
import com.example.EECToronto.Teams.TeamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class TeamMembersService {
    private final TeamMembersRepository teamMembersRepository;
    private final TeamsRepository teamsRepository;
    private final MembersRepository membersRepository;

    @Autowired
    public TeamMembersService(TeamMembersRepository teamMembersRepository, TeamsRepository teamsRepository, MembersRepository membersRepository) {
        this.teamMembersRepository = teamMembersRepository;
        this.teamsRepository = teamsRepository;
        this.membersRepository = membersRepository;
    }

    public List<TeamMembers> getAllTeamMembersService() {
        return teamMembersRepository.findAll();
    }

//    public List<Teams> getTeamsByMemberService(Long member_id) {
//        Members member = membersRepository.findById(member_id).orElseThrow(() -> new IllegalArgumentException("Invalid Member Id: " + member_id));
//        return teamMembersRepository.findTeamsByMember(member).
//                stream()
//                .map(TeamMembers::getTeam).toList();
//
//    }

    public List<Teams> getTeamsByMembersService(Long member_id) {
        Members members = membersRepository.findById(member_id).orElseThrow(() -> new IllegalArgumentException("Invalid Member Id: " + member_id));
        return teamMembersRepository.findTeamsByMembers(members).
                stream().
                map(TeamMembers::getTeam).
                toList();
    }

    public List<Members> getMembersByTeamsService(Long team_id) {
        Teams teams = teamsRepository.findById(team_id).orElseThrow(() -> new IllegalArgumentException("Invalid Teams Id: " + team_id));
        return teamMembersRepository.findMembersByTeams(teams)
                .stream()
                .map(TeamMembers::getMembers)
                .toList();
    }

//    public List<Members> getTeamMembersService(Long team_id) {
//        Teams teams = teamsRepository.findById(team_id).orElseThrow(() -> new IllegalArgumentException("Invalid Team Id: " + team_id));
////        teamMembersRepository.getTeamMembers(team_id);
//
//    }



    public void addMemberToTeam(Long team_id, Long member_id) {
        System.out.println("*****team_id:- " + team_id);
        Teams team = teamsRepository.findById(team_id).orElseThrow(() -> new IllegalArgumentException("Invalid Team Id: " + team_id));
        Members member = membersRepository.findById(member_id).orElseThrow(() -> new IllegalArgumentException("Invalid Member Id: " + member_id));

        TeamMembers teamMember = new TeamMembers(team, member);
        teamMembersRepository.save(teamMember);
    }
}
