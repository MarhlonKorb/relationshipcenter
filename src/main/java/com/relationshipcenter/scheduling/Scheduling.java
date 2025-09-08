package com.relationshipcenter.scheduling;

import com.relationshipcenter.services.RequestAssistenceService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class Scheduling {

    private final RequestAssistenceService requestAssistenceService;

    public Scheduling(RequestAssistenceService requestAssistenceService) {
        this.requestAssistenceService = requestAssistenceService;
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    public void scheduledNotifyAttendants() {
        requestAssistenceService.distributeRequest();
    }

    @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.SECONDS)
    public void finalizeRequestAttendant() {
        requestAssistenceService.finalizeRequest();
    }
}
