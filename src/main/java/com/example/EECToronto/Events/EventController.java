package com.example.EECToronto.Events;

import com.example.EECToronto.DTO.EventRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api/admin/events")
public class EventController {
    private final EventService eventService;
    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
    @GetMapping
    public List<Events> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public Optional<Events> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @GetMapping("/type/{event_type}")
    public List<Events> getEventByType(@PathVariable String event_type) {
        return eventService.getEventByType(event_type);
    }

    @PostMapping
    public ResponseEntity<Events> addEvent(@ModelAttribute EventRequestDTO eventRequestDTO) throws IOException {
        Events created = eventService.addEventsService(eventRequestDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Events> updateEvent(@PathVariable Long id, @RequestBody Events event) {
        Events updated = eventService.updateEvent(id, event);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Events> updateEventPartial(@PathVariable Long id, @ModelAttribute EventRequestDTO eventRequestDTO) throws IOException {
        Events updated = eventService.updateEventWithImage(id, eventRequestDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
