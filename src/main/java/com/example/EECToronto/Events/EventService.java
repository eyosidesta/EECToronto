package com.example.EECToronto.Events;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
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
    public void addEventsService(Events events) {
        eventRepository.save(events);
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
