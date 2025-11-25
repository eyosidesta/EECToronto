package com.example.EECToronto.Events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Events, Long> {
    List<Events> findByEventType(String eventType);
    List<Events> findByEventDateBefore(LocalDateTime date);
}
