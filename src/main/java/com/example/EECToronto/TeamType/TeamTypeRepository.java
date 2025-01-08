package com.example.EECToronto.TeamType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamTypeRepository extends JpaRepository<TeamType, Long> {
}
