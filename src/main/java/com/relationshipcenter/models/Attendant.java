package com.relationshipcenter.models;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Atendente
 */
public class Attendant {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    private Long id = COUNTER.incrementAndGet();

    private String name;

    private Team team;

    public Attendant(String name, Team team) {
        this.name = name;
        this.team = team;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Attendant setName(String name) {
        this.name = name;
        return this;
    }

    public Team team() {
        return team;
    }

    public Attendant setTeam(Team team) {
        this.team = team;
        return this;
    }

}
