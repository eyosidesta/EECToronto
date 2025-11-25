package com.example.EECToronto.SermonGroup;

import com.example.EECToronto.DTO.SermonGroupRequestDTO;
import com.example.EECToronto.DTO.SermonType;
import com.example.EECToronto.SermonGroupRship.SermonGroupRship;
import com.example.EECToronto.SermonGroupRship.SermonGroupRshipRepository;
import com.example.EECToronto.Sermons.SermonRepository;
import com.example.EECToronto.Sermons.Sermons;
import com.example.EECToronto.config.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class  SermonGroupService {
    private final SermonGroupRepository sermonGroupRepository;
    private final S3Service s3Service;
    private final SermonRepository sermonRepository;
    private final SermonGroupRshipRepository rshipRepository;

    public SermonGroupService(SermonGroupRepository sermonGroupRepository, 
                              S3Service s3Service,
                              SermonRepository sermonRepository,
                              SermonGroupRshipRepository rshipRepository) {
        this.sermonGroupRepository = sermonGroupRepository;
        this.s3Service = s3Service;
        this.sermonRepository = sermonRepository;
        this.rshipRepository = rshipRepository;
    }

    public List<SermonGroup> getAllGroups() {
        return sermonGroupRepository.findAll();
    }

    public Optional<SermonGroup> getGroupById(Long id) {
        return sermonGroupRepository.findById(id);
    }

    public List<SermonGroup> getGroupsByType(SermonType sermonType) {
        return sermonGroupRepository.findBySermonType(sermonType);
    }

    public SermonGroup createSermonGroup(SermonGroupRequestDTO dto) throws IOException {
        MultipartFile image = dto.getImage();
        String imageUrl = null;
        
        if (image != null && !image.isEmpty()) {
            String fileName = "sermon-group_" + System.currentTimeMillis() + "_" + image.getOriginalFilename();
            imageUrl = s3Service.uploadFile(fileName, image.getBytes());
        }

        SermonType sermonType = SermonType.valueOf(dto.getType().toUpperCase());
        
        SermonGroup group = new SermonGroup();
        group.setName(dto.getTitle());
        group.setDescription(dto.getExplanation());
        group.setSermonType(sermonType);
        group.setImageUrl(imageUrl);

        return sermonGroupRepository.save(group);
    }

    public SermonGroup updateGroup(Long id, SermonGroupRequestDTO dto) throws IOException {
        return sermonGroupRepository.findById(id)
                .map(group -> {
                    group.setName(dto.getTitle());
                    group.setDescription(dto.getExplanation());
                    
                    if (dto.getType() != null) {
                        SermonType sermonType = SermonType.valueOf(dto.getType().toUpperCase());
                        group.setSermonType(sermonType);
                    }

                    // Handle image update if provided
                    MultipartFile image = dto.getImage();
                    if (image != null && !image.isEmpty()) {
                        try {
                            String fileName = "sermon-group_" + System.currentTimeMillis() + "_" + image.getOriginalFilename();
                            String imageUrl = s3Service.uploadFile(fileName, image.getBytes());
                            group.setImageUrl(imageUrl);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to upload image: " + e.getMessage());
                        }
                    }

                    return sermonGroupRepository.save(group);
                }).orElseThrow(() -> new RuntimeException("Group not found"));
    }

    public SermonGroup updateGroupSimple(Long id, SermonGroup sermonGroup) {
        return sermonGroupRepository.findById(id)
                .map(group -> {
                    group.setName(sermonGroup.getName());
                    group.setDescription(sermonGroup.getDescription());
                    group.setSermonType(sermonGroup.getSermonType());
                    if (sermonGroup.getImageUrl() != null) {
                        group.setImageUrl(sermonGroup.getImageUrl());
                    }
                    return sermonGroupRepository.save(group);
                }).orElseThrow(() -> new RuntimeException("Group not found"));
    }

    public void deleteSermonGroup(Long id) {
        sermonGroupRepository.deleteById(id);
    }

    public SermonGroupRship addSermonToGroup(Long groupId, Long sermonId) {
        Optional<SermonGroup> groupOpt = sermonGroupRepository.findById(groupId);
        if (groupOpt.isEmpty()) {
            throw new RuntimeException("Group not found");
        }
        
        Optional<Sermons> sermonOpt = sermonRepository.findById(sermonId);
        if (sermonOpt.isEmpty()) {
            throw new RuntimeException("Sermon not found");
        }
        
        SermonGroup group = groupOpt.get();
        Sermons sermon = sermonOpt.get();
        
        // Ensure sermon type matches group type
        if (!sermon.getSermonType().equals(group.getSermonType())) {
            throw new RuntimeException("Sermon type mismatch: Sermon is " + 
                sermon.getSermonType() + ", but Group requires " + group.getSermonType());
        }
        
        // Check if relationship already exists
        SermonGroupRship existing = rshipRepository.findBySermonsAndGroup(sermon, group);
        if (existing != null) {
            throw new RuntimeException("This sermon is already in the group.");
        }
        
        // Create new relationship
        SermonGroupRship rship = new SermonGroupRship();
        rship.setSermons(sermon);
        rship.setGroup(group);
        
        return rshipRepository.save(rship);
    }

    public void removeSermonFromGroup(Long groupId, Long sermonId) {
        Optional<SermonGroup> groupOpt = sermonGroupRepository.findById(groupId);
        Optional<Sermons> sermonOpt = sermonRepository.findById(sermonId);
        
        if (groupOpt.isEmpty() || sermonOpt.isEmpty()) {
            throw new RuntimeException("Group or Sermon not found");
        }
        
        SermonGroupRship rship = rshipRepository.findBySermonsAndGroup(sermonOpt.get(), groupOpt.get());
        if (rship != null) {
            rshipRepository.delete(rship);
        }
    }

    public Optional<List<Sermons>> getSermonsInGroup(Long groupId) {
        return sermonGroupRepository.findById(groupId)
                .map(group -> group.getSermons().stream()
                        .map(SermonGroupRship::getSermons)
                        .collect(Collectors.toList()));
    }
}
