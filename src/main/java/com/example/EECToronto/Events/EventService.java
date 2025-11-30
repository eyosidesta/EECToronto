package com.example.EECToronto.Events;

import com.example.EECToronto.DTO.EventRequestDTO;
import com.example.EECToronto.config.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final S3Service s3Service;
    public EventService(EventRepository eventRepository, S3Service s3Service) {
        this.eventRepository = eventRepository;
        this.s3Service = s3Service;
    }
    public List<Events> getAllEvents() {
        return eventRepository.findAll();
    }
    public Optional<Events> getEventById(Long id) {
        return eventRepository.findById(id);
    }
    public List<Events> getEventByType(String event_type) {
        return eventRepository.findByEventType(event_type);
    }
    public Events addEventsService(@ModelAttribute EventRequestDTO dto) throws IOException {
        MultipartFile image = dto.getEventImage();
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            imageUrl = s3Service.uploadFile(fileName, image.getBytes());
        }
        Events events = new Events(
                dto.getEventTitle(),
                dto.getEventDescription(),
                imageUrl,
                dto.getEventStreet(),
                dto.getEventCity(),
                dto.getEventType(),
                dto.getEventDate()
        );
        return eventRepository.save(events);
    }
    public Events updateEvent(Long id, Events updatedEvent) {
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    existingEvent.setEventTitle(updatedEvent.getEventTitle());
                    existingEvent.setEventDescription(updatedEvent.getEventDescription());
                    existingEvent.setEventImageUrl(updatedEvent.getEventImageUrl());
                    existingEvent.setEventCity(updatedEvent.getEventCity());
                    existingEvent.setEventStreet(updatedEvent.getEventStreet());
                    existingEvent.setEventType((updatedEvent.getEventType()));
                    existingEvent.setEventDate(updatedEvent.getEventDate());
                    return eventRepository.save(existingEvent);
                }).orElseThrow(() -> new RuntimeException("Event Not Found with id " + id));
    }

    public Events updateEventWithImage(Long id, @ModelAttribute EventRequestDTO dto) throws IOException {
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    // Update basic fields
                    if (dto.getEventTitle() != null) {
                        existingEvent.setEventTitle(dto.getEventTitle());
                    }
                    if (dto.getEventDescription() != null) {
                        existingEvent.setEventDescription(dto.getEventDescription());
                    }
                    if (dto.getEventStreet() != null) {
                        existingEvent.setEventStreet(dto.getEventStreet());
                    }
                    if (dto.getEventCity() != null) {
                        existingEvent.setEventCity(dto.getEventCity());
                    }
                    if (dto.getEventType() != null) {
                        existingEvent.setEventType(dto.getEventType());
                    }
                    if (dto.getEventDate() != null) {
                        existingEvent.setEventDate(dto.getEventDate());
                    }

                    // Handle image
                    MultipartFile image = dto.getEventImage();
                    if (image != null && !image.isEmpty()) {
                        // New image uploaded
                        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                        String imageUrl = s3Service.uploadFile(fileName, image.getBytes());
                        existingEvent.setEventImageUrl(imageUrl);
                    } else if (dto.getEventImageUrl() != null && !dto.getEventImageUrl().trim().isEmpty()) {
                        // Keep existing image URL if provided
                        existingEvent.setEventImageUrl(dto.getEventImageUrl());
                    }

                    return eventRepository.save(existingEvent);
                }).orElseThrow(() -> new RuntimeException("Event Not Found with id " + id));
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    /**
     * Delete events that are past their event date by more than 1 day
     * This is called by a scheduled task (EventScheduledTask)
     */
    public int deletePastEvents() {
        // Get current date/time minus 1 day (events that are at least 1 day past their event date)
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        
        // Find all events with eventDate before the cutoff date
        List<Events> pastEvents = eventRepository.findAll().stream()
                .filter(event -> event.getEventDate() != null && event.getEventDate().isBefore(oneDayAgo))
                .toList();

        // Delete all past events
        int count = pastEvents.size();
        if (count > 0) {
            eventRepository.deleteAll(pastEvents);
        }
        
        return count;
    }

}
