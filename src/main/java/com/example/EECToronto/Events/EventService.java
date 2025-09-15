package com.example.EECToronto.Events;

import com.example.EECToronto.DTO.EventRequestDTO;
import com.example.EECToronto.config.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public Events addEventsService(EventRequestDTO dto) throws IOException {
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

}
