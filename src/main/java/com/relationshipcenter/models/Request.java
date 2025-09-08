package com.relationshipcenter.models;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Solicitações
 */
public class Request {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    private Long id = COUNTER.incrementAndGet();

    private Subject subject;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Request() {
    }

    public Request(Subject subject) {
        this.subject = subject;
    }

    public Long id() {
        return id;
    }

    public Subject subject() {
        return subject;
    }

    public Request setSubject(Subject subject) {
        this.subject = subject;
        return this;
    }

}
