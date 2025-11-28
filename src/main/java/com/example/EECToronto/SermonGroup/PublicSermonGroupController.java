package com.example.EECToronto.SermonGroup;

import com.example.EECToronto.DTO.SermonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sermon-groups")
@CrossOrigin(origins = {"https://www.geecvancouver.ca", "https://geecvancouver.ca", "https://geecvancouver.vercel.app", "http://localhost:3036"})
public class PublicSermonGroupController {
    private final SermonGroupService sermonGroupService;

    @Autowired
    public PublicSermonGroupController(SermonGroupService sermonGroupService) {
        this.sermonGroupService = sermonGroupService;
    }

    // Public GET endpoints - no authentication required
    @GetMapping
    public List<SermonGroup> getAllGroups() {
        return sermonGroupService.getAllGroups();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SermonGroup> getGroupById(@PathVariable Long id) {
        return sermonGroupService.getGroupById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public List<SermonGroup> getGroupsByType(@PathVariable String type) {
        try {
            SermonType sermonType = SermonType.valueOf(type.toUpperCase());
            return sermonGroupService.getGroupsByType(sermonType);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid sermon type: " + type);
        }
    }

    // Get sermons in a specific group - public endpoint
    @GetMapping("/{groupId}/sermons")
    public ResponseEntity<?> getSermonsInGroup(@PathVariable Long groupId) {
        return sermonGroupService.getSermonsInGroup(groupId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

