package com.example.EECToronto.Sermons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://geecvancouver.vercel.app",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
@RestController
@RequestMapping(path="api/sermons")
public class SermonController {
    private final SermonService sermonService;
    @Autowired
    public SermonController(SermonService sermonService) {
        this.sermonService = sermonService;
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

    @PostMapping
    public void createSermon(@RequestBody Sermons sermons) {
         sermonService.createSermon(sermons);
    }

    @PutMapping("/{id}")
    public Sermons updateSermon(@PathVariable Long id, @RequestBody Sermons sermons) {
        return sermonService.updateSermon(id, sermons);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSermon(@PathVariable Long id) {
        sermonService.deleteSermon(id);
        return ResponseEntity.noContent().build();
    }

}
