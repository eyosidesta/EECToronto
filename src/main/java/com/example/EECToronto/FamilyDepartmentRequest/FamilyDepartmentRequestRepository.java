package com.example.EECToronto.FamilyDepartmentRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyDepartmentRequestRepository extends JpaRepository<FamilyDepartmentRequest, Long> {
    List<FamilyDepartmentRequest> findByContacted(boolean contacted);
    List<FamilyDepartmentRequest> findAllByOrderByRequestDateDesc();
    Optional<FamilyDepartmentRequest> findByPhoneAndContacted(String phone, boolean contacted);
    Optional<FamilyDepartmentRequest> findByEmailAndContacted(String email, boolean contacted);
}


