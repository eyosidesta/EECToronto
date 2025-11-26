package com.example.EECToronto.SermonGroupRship;

import com.example.EECToronto.DTO.SermonGroupRshipRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("https://geecvancouver.vercel.app")
@RestController
@RequestMapping("api/admin/sermon-group-rship")
public class SermonGroupRshipController {
    private final SermonGroupRshipService rshipService;
    @Autowired
    public SermonGroupRshipController(SermonGroupRshipService rshipService) {
        this.rshipService = rshipService;
    }

    @GetMapping
    public List<SermonGroupRship> getAllRships() {
        return rshipService.getAllRships();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SermonGroupRship> getRshipById(@PathVariable Long id) {
        return rshipService.getRshipById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SermonGroupRship createRship(@RequestBody SermonGroupRshipRequest request) {
        return rshipService.createRship(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRship(@PathVariable Long id) {
        rshipService.deleteRship(id);
        return ResponseEntity.noContent().build();
    }
}
