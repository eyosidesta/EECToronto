package com.example.EECToronto.Sermons;

import com.example.EECToronto.Admin.Admin;
import com.example.EECToronto.Admin.AdminRepository;
import com.example.EECToronto.DTO.SermonType;
import com.example.EECToronto.auth.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api/admin/sermons")
public class SermonController {
    private final SermonService sermonService;
    private final AdminRepository adminRepository;
    private final JwtService jwtService;
    
    @Autowired
    public SermonController(SermonService sermonService, AdminRepository adminRepository, JwtService jwtService) {
        this.sermonService = sermonService;
        this.adminRepository = adminRepository;
        this.jwtService = jwtService;
    }

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
        SermonType sermonType = SermonType.valueOf(type.toUpperCase());
        return sermonService.getSermonByType(sermonType);
    }

    @PostMapping
    public ResponseEntity<Sermons> createSermon(@RequestBody Sermons sermons, HttpServletRequest request) {
        String adminName = extractAdminName(request);
        Sermons created = sermonService.createSermon(sermons, adminName);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sermons> updateSermon(@PathVariable Long id, @RequestBody Sermons sermons) {
        Sermons updated = sermonService.updateSermon(id, sermons);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Sermons> partialUpdateSermon(@PathVariable Long id, @RequestBody Sermons sermons) {
        return sermonService.getSermonById(id)
                .map(existing -> {
                    if (sermons.getPreacherName() != null) existing.setPreacherName(sermons.getPreacherName());
                    if (sermons.getSermonTitle() != null) existing.setSermonTitle(sermons.getSermonTitle());
                    if (sermons.getYoutubeLink() != null) existing.setYoutubeLink(sermons.getYoutubeLink());
                    if (sermons.getSermonDate() != null) existing.setSermonDate(sermons.getSermonDate());
                    if (sermons.getSermonType() != null) existing.setSermonType(sermons.getSermonType());
                    return ResponseEntity.ok(sermonService.updateSermon(id, existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSermon(@PathVariable Long id) {
        sermonService.deleteSermon(id);
        return ResponseEntity.noContent().build();
    }

    private String extractAdminName(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = jwtService.extractUsername(token);
                Optional<Admin> adminOpt = adminRepository.findByUsername(username);
                if (adminOpt.isPresent()) {
                    return adminOpt.get().getName();
                }
            }
        } catch (Exception e) {
            // If we can't extract admin name, return null (will be stored as null)
        }
        return null;
    }
}
