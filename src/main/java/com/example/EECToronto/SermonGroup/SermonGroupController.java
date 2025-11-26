package com.example.EECToronto.SermonGroup;

import com.example.EECToronto.DTO.SermonGroupRequestDTO;
import com.example.EECToronto.DTO.SermonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/admin/sermon-groups")
public class SermonGroupController {
    private final SermonGroupService sermonGroupService;
    
    @Autowired
    public SermonGroupController(SermonGroupService sermonGroupService) {
        this.sermonGroupService = sermonGroupService;
    }

    @GetMapping
    public List<SermonGroup> getAllGrouops() {
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
        SermonType sermonType = SermonType.valueOf(type.toUpperCase());
        return sermonGroupService.getGroupsByType(sermonType);
    }

    @PostMapping
    public ResponseEntity<SermonGroup> createGroup(@ModelAttribute SermonGroupRequestDTO dto) throws IOException {
        SermonGroup created = sermonGroupService.createSermonGroup(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SermonGroup> updateSermonGroup(@PathVariable Long id, @ModelAttribute SermonGroupRequestDTO dto) throws IOException {
        SermonGroup updated = sermonGroupService.updateGroup(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        sermonGroupService.deleteSermonGroup(id);
        return ResponseEntity.noContent().build();
    }

    // Get sermons in a specific group
    @GetMapping("/{groupId}/sermons")
    public ResponseEntity<?> getSermonsInGroup(@PathVariable Long groupId) {
        return sermonGroupService.getSermonsInGroup(groupId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Add sermon to group
    @PostMapping("/{groupId}/sermons/{sermonId}")
    public ResponseEntity<?> addSermonToGroup(@PathVariable Long groupId, @PathVariable Long sermonId) {
        try {
            return ResponseEntity.ok(sermonGroupService.addSermonToGroup(groupId, sermonId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Remove sermon from group (not delete sermon, just remove relationship)
    @DeleteMapping("/{groupId}/sermons/{sermonId}")
    public ResponseEntity<Void> removeSermonFromGroup(@PathVariable Long groupId, @PathVariable Long sermonId) {
        sermonGroupService.removeSermonFromGroup(groupId, sermonId);
        return ResponseEntity.noContent().build();
    }
}
