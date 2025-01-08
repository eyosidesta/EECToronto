package com.example.EECToronto.TeamMembers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMembersRepository extends JpaRepository<TeamMembers, Long> {
//    List<TeamMembers> getMemberTeams(Long member_id);
}
