package com.example.EECToronto.Sermons;

import com.example.EECToronto.DTO.SermonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sermons")
@CrossOrigin(origins = {"https://www.geecvancouver.ca", "https://geecvancouver.ca", "https://geecvancouver.vercel.app", "http://localhost:3036"})
public class PublicSermonController {
    private final SermonService sermonService;

    @Autowired
    public PublicSermonController(SermonService sermonService) {
        this.sermonService = sermonService;
    }

    // Public GET endpoints - no authentication required
    @GetMapping
    public List<Sermons> getAllSermons() {
        return sermonService.getAllSermons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sermons> getSermonById(@PathVariable Long id) {
        return sermonService.getSermonById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public List<Sermons> getSermonsByType(@PathVariable String type) {
        try {
            SermonType sermonType = SermonType.valueOf(type.toUpperCase());
            return sermonService.getSermonByType(sermonType);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid sermon type: " + type);
        }
    }
}

