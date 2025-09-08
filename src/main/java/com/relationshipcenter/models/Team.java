package com.relationshipcenter.models;

import java.util.concurrent.atomic.AtomicLong;

public class Team {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    private Long id = COUNTER.incrementAndGet();

    private AttendanceType attendanceType;

    private boolean isDefault;

    public AttendanceType attendanceType() {
        return attendanceType;
    }

    public Team setAssistanceType(AttendanceType attendanceType) {
        this.attendanceType = attendanceType;
        return this;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public Team setDefault(boolean aDefault) {
        isDefault = aDefault;
        return this;
    }

}
