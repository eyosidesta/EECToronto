package com.example.EECToronto.Teams;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamsRepository extends JpaRepository<Teams, Long> {
    @Query("SELECT t FROM Teams t WHERE LOWER(t.team_name) = LOWER(:teamName)")
    Optional<Teams> findByTeam_nameIgnoreCase(@Param("teamName") String teamName);
}
