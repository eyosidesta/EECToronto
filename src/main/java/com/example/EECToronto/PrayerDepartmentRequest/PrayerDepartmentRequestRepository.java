package com.example.EECToronto.PrayerDepartmentRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrayerDepartmentRequestRepository extends JpaRepository<PrayerDepartmentRequest, Long> {
    List<PrayerDepartmentRequest> findByContacted(boolean contacted);
    List<PrayerDepartmentRequest> findAllByOrderByRequestDateDesc();
    List<PrayerDepartmentRequest> findByPhoneAndContacted(String phone, boolean contacted);
    List<PrayerDepartmentRequest> findByEmailAndContacted(String email, boolean contacted);
}

