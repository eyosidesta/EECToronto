package com.example.EECToronto.AcceptChristRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcceptChristRequestRepository extends JpaRepository<AcceptChristRequest, Long> {
    List<AcceptChristRequest> findByContacted(boolean contacted);
    List<AcceptChristRequest> findAllByOrderByRequestDateDesc();
    Optional<AcceptChristRequest> findByPhoneAndContacted(String phone, boolean contacted);
    Optional<AcceptChristRequest> findByEmailAndContacted(String email, boolean contacted);
}



