package com.relationshipcenter.models;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Assunto
 */
public class Subject {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    private Long id = COUNTER.incrementAndGet();

    private String description;

    public Subject(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;
        Subject subject = (Subject) o;
        return description.equals(subject.description);
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }
}
