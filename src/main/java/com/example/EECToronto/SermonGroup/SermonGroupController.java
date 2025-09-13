package com.example.EECToronto.SermonGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3036")
@RestController
@RequestMapping("api/sermon-groups")
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

    @PostMapping
    public SermonGroup createGroup(@RequestBody SermonGroup group) {
        return sermonGroupService.createSermonGroup(group);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SermonGroup> updateSermonGroup(@PathVariable Long id, @RequestBody SermonGroup group) {
        return ResponseEntity.ok(sermonGroupService.updateGroup(id, group));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        sermonGroupService.deleteSermonGroup(id);
        return ResponseEntity.noContent().build();
    }
}
