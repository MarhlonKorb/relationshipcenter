package com.relationshipcenter.models;

import java.util.concurrent.atomic.AtomicLong;

public class AttendantRequest {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    private Long id = COUNTER.incrementAndGet();

    private Attendant attendant;

    private Request request;

    public AttendantRequest(Attendant attendant, Request request) {
        this.attendant = attendant;
        this.request = request;
    }

    public Long id() {
        return id;
    }

    public AttendantRequest setId(Long id) {
        this.id = id;
        return this;
    }

    public Attendant attendant() {
        return attendant;
    }

    public AttendantRequest setAttendant(Attendant attendant) {
        this.attendant = attendant;
        return this;
    }

    public Request request() {
        return request;
    }

    public String requestSubjectSubscription(){
        return this.request().subject().description();
    }
}
