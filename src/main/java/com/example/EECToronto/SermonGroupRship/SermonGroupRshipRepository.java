package com.example.EECToronto.SermonGroupRship;

import com.example.EECToronto.SermonGroup.SermonGroup;
import com.example.EECToronto.SermonGroup.SermonGroup;
import com.example.EECToronto.Sermons.Sermons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SermonGroupRshipRepository extends JpaRepository<SermonGroupRship, Long> {
    List<SermonGroupRship> findBySermons(Sermons sermons);

    List<SermonGroupRship> findByGroup(SermonGroup group);

    SermonGroupRship findBySermonsAndGroup(Sermons sermons, SermonGroup group);
}
