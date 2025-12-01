package com.example.EECToronto.ChildrenDepartmentRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChildrenDepartmentRequestRepository extends JpaRepository<ChildrenDepartmentRequest, Long> {
    List<ChildrenDepartmentRequest> findByContacted(boolean contacted);
    List<ChildrenDepartmentRequest> findAllByOrderByRequestDateDesc();
    Optional<ChildrenDepartmentRequest> findByPhoneAndContacted(String phone, boolean contacted);
    Optional<ChildrenDepartmentRequest> findByEmailAndContacted(String email, boolean contacted);
}



