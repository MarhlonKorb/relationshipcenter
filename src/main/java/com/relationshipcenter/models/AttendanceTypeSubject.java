package com.relationshipcenter.models;

import java.util.concurrent.atomic.AtomicLong;

public class AttendanceTypeSubject {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    private Long id = COUNTER.incrementAndGet();

    private AttendanceType attendanceType;

    private Subject acceptedSubject;

    public AttendanceTypeSubject(AttendanceType attendanceType, Subject acceptedSubject) {
        this.attendanceType = attendanceType;
        this.acceptedSubject = acceptedSubject;
    }

    public AttendanceType attendanceType() {
        return attendanceType;
    }

    public Subject acceptedSubject() {
        return acceptedSubject;
    }

}
