package com.relationshipcenter.repositories;

import com.relationshipcenter.models.Team;
import com.relationshipcenter.shared.RepositoryGen;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class TeamRepositoryMock implements RepositoryGen<Team, Long> {

    private final List<Team> teams = new ArrayList<>();

    public List<Team> list() {
        return Collections.unmodifiableList(teams);
    }

    public void create(Team team) {
        teams.add(team);
    }
}
