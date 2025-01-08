package com.example.EECToronto.TeamSMS;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamSMSRepository extends JpaRepository<TeamSMS, Long> {
}
