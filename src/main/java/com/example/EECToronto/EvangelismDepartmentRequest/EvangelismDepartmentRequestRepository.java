package com.example.EECToronto.EvangelismDepartmentRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvangelismDepartmentRequestRepository extends JpaRepository<EvangelismDepartmentRequest, Long> {
    List<EvangelismDepartmentRequest> findByContacted(boolean contacted);
    List<EvangelismDepartmentRequest> findAllByOrderByRequestDateDesc();
    Optional<EvangelismDepartmentRequest> findByPhoneAndContacted(String phone, boolean contacted);
    Optional<EvangelismDepartmentRequest> findByEmailAndContacted(String email, boolean contacted);
}



