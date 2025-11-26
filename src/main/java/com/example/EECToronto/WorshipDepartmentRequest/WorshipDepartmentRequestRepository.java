package com.example.EECToronto.WorshipDepartmentRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorshipDepartmentRequestRepository extends JpaRepository<WorshipDepartmentRequest, Long> {
    List<WorshipDepartmentRequest> findByContacted(boolean contacted);
    List<WorshipDepartmentRequest> findAllByOrderByRequestDateDesc();
    Optional<WorshipDepartmentRequest> findByPhoneAndContacted(String phone, boolean contacted);
    Optional<WorshipDepartmentRequest> findByEmailAndContacted(String email, boolean contacted);
}

