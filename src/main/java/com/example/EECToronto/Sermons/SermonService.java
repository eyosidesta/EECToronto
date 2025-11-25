package com.example.EECToronto.Sermons;

import com.example.EECToronto.DTO.SermonType;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SermonService {
    private final SermonRepository sermonRepository;
    
    public SermonService(SermonRepository sermonRepository) {
        this.sermonRepository = sermonRepository;
    }

    public List<Sermons> getAllSermons() {
        return sermonRepository.findAll();
    }

    public Optional<Sermons> getSermonById(Long id) {
        return sermonRepository.findById(id);
    }
    
    public List<Sermons> getSermonByType(SermonType sermonType) {
        return sermonRepository.findBySermonType(sermonType);
    }

    public Sermons createSermon(Sermons sermons, String adminName) {
        sermons.setCreatedByAdminName(adminName);
        return sermonRepository.save(sermons);
    }

    public Sermons updateSermon(Long id, Sermons updateSermon) {
        return sermonRepository.findById(id)
                .map(existingSermon -> {
                    existingSermon.setPreacherName(updateSermon.getPreacherName());
                    existingSermon.setSermonTitle(updateSermon.getSermonTitle());
                    existingSermon.setSermonDate(updateSermon.getSermonDate());
                    existingSermon.setSermonType(updateSermon.getSermonType());
                    if (updateSermon.getYoutubeLink() != null) {
                        existingSermon.setYoutubeLink(updateSermon.getYoutubeLink());
                    }
                    // Don't update createdByAdminName on update - keep original creator
                    return sermonRepository.save(existingSermon);
                }).orElseThrow(() -> new RuntimeException("Sermon Not Found with Id" + id));
    }

    public void deleteSermon(Long id) {
        sermonRepository.deleteById(id);
    }
}
