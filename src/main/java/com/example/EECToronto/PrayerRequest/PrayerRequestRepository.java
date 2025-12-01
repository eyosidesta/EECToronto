package com.example.EECToronto.PrayerRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrayerRequestRepository extends JpaRepository<PrayerRequest, Long> {
    List<PrayerRequest> findByContacted(boolean contacted);
    List<PrayerRequest> findAllByOrderByRequestDateDesc();
    long countByContacted(boolean contacted);
}



