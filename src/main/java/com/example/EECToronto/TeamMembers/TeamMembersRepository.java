package com.example.EECToronto.TeamMembers;

import com.example.EECToronto.Members.Members;
import com.example.EECToronto.Teams.Teams;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMembersRepository extends JpaRepository<TeamMembers, Long> {
    List<TeamMembers> findTeamsByMembers(Members members);
    List<TeamMembers> findMembersByTeams(Teams teams);
    boolean existsByTeamsAndMembers(Teams teams, Members members);
}
