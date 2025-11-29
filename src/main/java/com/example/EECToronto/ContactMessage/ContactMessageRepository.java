package com.example.EECToronto.ContactMessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    List<ContactMessage> findByContacted(boolean contacted);
    List<ContactMessage> findAllByOrderByRequestDateDesc();
    long countByContacted(boolean contacted);
}

