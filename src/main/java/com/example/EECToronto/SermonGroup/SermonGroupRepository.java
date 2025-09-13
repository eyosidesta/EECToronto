package com.example.EECToronto.SermonGroup;

import com.example.EECToronto.DTO.SermonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface SermonGroupRepository extends JpaRepository<SermonGroup, Long> {
    List<SermonGroup> findBySermonType(SermonType SermonType);

    List<SermonGroup> findByNameContainingIgnoreCase(String name);
}
