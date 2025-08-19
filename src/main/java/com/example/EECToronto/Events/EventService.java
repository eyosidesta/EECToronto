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
    public void addEventsService(Events events) {
        eventRepository.save(events);
    }
    public Events updateEvent(Long id, Events updatedEvent) {
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    existingEvent.setEvent_title(updatedEvent.getEvent_title());
                    existingEvent.setEvent_description(updatedEvent.getEvent_description());
                    existingEvent.setEvent_imageUrl(updatedEvent.getEvent_imageUrl());
                    existingEvent.setEvent_place(updatedEvent.getEvent_place());
                    existingEvent.setEvent_date(updatedEvent.getEvent_date());
                    return eventRepository.save(existingEvent);
                }).orElseThrow(() -> new RuntimeException("Event Not Found with id " + id));
    }

}
