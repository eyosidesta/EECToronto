package com.example.EECToronto.SermonGroupRship;

import com.example.EECToronto.DTO.SermonGroupRshipRequest;
import com.example.EECToronto.SermonGroup.SermonGroup;
import com.example.EECToronto.SermonGroup.SermonGroupRepository;
import com.example.EECToronto.SermonGroup.SermonGroupService;
import com.example.EECToronto.Sermons.SermonRepository;
import com.example.EECToronto.Sermons.Sermons;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SermonGroupRshipService {
    private final SermonGroupRshipRepository rshipRepository;
    private final SermonRepository sermonRepository;
    private final SermonGroupRepository sermonGroupRepository;

    public SermonGroupRshipService(SermonGroupRshipRepository rshipRepository, SermonRepository sermonRepository, SermonGroupRepository sermonGroupRepository) {
        this.rshipRepository = rshipRepository;
        this.sermonRepository = sermonRepository;
        this.sermonGroupRepository = sermonGroupRepository;
    }

    public List<SermonGroupRship> getAllRships() {
        return rshipRepository.findAll();
    }

    public Optional<SermonGroupRship> getRshipById(Long id) {
        return rshipRepository.findById(id);
    }
public SermonGroupRship createRship(SermonGroupRshipRequest request) {
    Sermons sermon = sermonRepository.findById(request.getSermonId())
            .orElseThrow(() -> new RuntimeException("Sermon not found"));
    SermonGroup group = sermonGroupRepository.findById(request.getGroupId())
            .orElseThrow(() -> new RuntimeException("Group not found"));

    // Ensure sermonType matches
    if (!sermon.getSermonType().equals(group.getSermonType())) {
        throw new RuntimeException("Sermon type mismatch: Sermon is "
                + sermon.getSermonType() + ", but Group requires " + group.getSermonType());
    }
    // prevent duplicate
    SermonGroupRship existing = rshipRepository.findBySermonsAndGroup(sermon, group);
    if (existing != null) {
        throw new RuntimeException("This sermon is already in the group.");
    }

    SermonGroupRship rship = new SermonGroupRship();
    rship.setSermons(sermon);
    rship.setGroup(group);

    return rshipRepository.save(rship);
}

    public void deleteRship(Long id) {
        rshipRepository.deleteById(id);
    }
}
