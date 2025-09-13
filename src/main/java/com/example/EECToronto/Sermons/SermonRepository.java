package com.example.EECToronto.Sermons;

import com.example.EECToronto.DTO.SermonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SermonRepository extends JpaRepository<Sermons, Long> {
    List<Sermons> findBySermonType(SermonType sermonType);
}
