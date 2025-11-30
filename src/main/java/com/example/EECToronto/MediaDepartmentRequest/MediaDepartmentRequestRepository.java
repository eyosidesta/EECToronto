package com.example.EECToronto.MediaDepartmentRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaDepartmentRequestRepository extends JpaRepository<MediaDepartmentRequest, Long> {
    List<MediaDepartmentRequest> findByContacted(boolean contacted);
    List<MediaDepartmentRequest> findAllByOrderByRequestDateDesc();
    Optional<MediaDepartmentRequest> findByPhoneAndContacted(String phone, boolean contacted);
    Optional<MediaDepartmentRequest> findByEmailAndContacted(String email, boolean contacted);
}


