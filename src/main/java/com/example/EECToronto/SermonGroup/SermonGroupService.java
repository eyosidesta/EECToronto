package com.example.EECToronto.SermonGroup;

import com.example.EECToronto.SermonGroupRship.SermonGroupRshipRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SermonGroupService {
    private final SermonGroupRepository sermonGroupRepository;

    public SermonGroupService(SermonGroupRepository sermonGroupRepository) {
        this.sermonGroupRepository = sermonGroupRepository;
    }

    public List<SermonGroup> getAllGroups() {
        return sermonGroupRepository.findAll();
    }

    public Optional<SermonGroup> getGroupById(Long id) {
        return sermonGroupRepository.findById(id);
    }

    public SermonGroup createSermonGroup(SermonGroup group) {
        return sermonGroupRepository.save(group);
    }

    public SermonGroup updateGroup(Long id, SermonGroup sermonGroup) {
        return sermonGroupRepository.findById(id)
                .map(group -> {
                    group.setName(sermonGroup.getName());
                    group.setDescription(sermonGroup.getDescription());
                    group.setSermonType(sermonGroup.getSermonType());
                    return sermonGroupRepository.save(group);
                }).orElseThrow(() -> new RuntimeException("Group not found"));
    }

    public void deleteSermonGroup(Long id) {
        sermonGroupRepository.deleteById(id);
    }
}
