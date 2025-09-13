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

    public void createSermon(Sermons sermons) {
        sermonRepository.save(sermons);
    }


    public Sermons updateSermon(Long id, Sermons updateSermon) {
        return sermonRepository.findById(id)
                .map(existingEvent -> {
                    existingEvent.setPreacherName(updateSermon.getPreacherName());
                    existingEvent.setSermonTitle(updateSermon.getSermonTitle());
                    existingEvent.setSermonDate(updateSermon.getSermonDate());
                    existingEvent.setSermonType(updateSermon.getSermonType());
                    return sermonRepository.save(existingEvent);
                }).orElseThrow(() -> new RuntimeException("Sermon Not Found with Id" + id));
    }

    public void deleteSermon(Long id) {
        sermonRepository.deleteById(id);
    }
}
