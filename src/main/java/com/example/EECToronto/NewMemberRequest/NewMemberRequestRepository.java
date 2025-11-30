package com.example.EECToronto.NewMemberRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewMemberRequestRepository extends JpaRepository<NewMemberRequest, Long> {
    List<NewMemberRequest> findByContacted(boolean contacted);
    List<NewMemberRequest> findAllByOrderByRequestDateDesc();
    Optional<NewMemberRequest> findByPhoneAndContacted(String phone, boolean contacted);
    Optional<NewMemberRequest> findByEmailAndContacted(String email, boolean contacted);
}


