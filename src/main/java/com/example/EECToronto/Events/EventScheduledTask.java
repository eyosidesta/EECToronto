package com.example.EECToronto.Events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EventScheduledTask {
    
    private static final Logger logger = LoggerFactory.getLogger(EventScheduledTask.class);
    
    private final EventService eventService;
    
    public EventScheduledTask(EventService eventService) {
        this.eventService = eventService;
    }
    
    /**
     * Scheduled task that runs every day at 2:00 AM to delete events
     * that are at least 1 day past their event date
     * 
     * Example: If today is Jan 10, events with eventDate before Jan 9 will be deleted
     * 
     * Cron format: second, minute, hour, day, month, weekday
     * "0 0 2 * * ?" means: at 2:00 AM every day
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void deletePastEvents() {
        try {
            logger.info("Starting scheduled task to delete past events (events older than 1 day)...");
            int deletedCount = eventService.deletePastEvents();
            if (deletedCount > 0) {
                logger.info("Scheduled task completed. Deleted {} past event(s).", deletedCount);
            } else {
                logger.info("Scheduled task completed. No past events to delete.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting past events: {}", e.getMessage(), e);
        }
    }
}

