package com.relationshipcenter.models;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Tipo de atendimento
 */
public class AttendanceType {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    private Long id = COUNTER.incrementAndGet();

    private String name;

    public AttendanceType() {
    }

    public AttendanceType(String name) {
        this.name = name;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public AttendanceType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttendanceType)) return false;
        return name.equals(((AttendanceType) o).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
