package com.relationshipcenter.repositories;

import com.relationshipcenter.models.Attendant;
import com.relationshipcenter.shared.RepositoryGen;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class AttendantRepositoryMock implements RepositoryGen<Attendant, Long> {

    private final List<Attendant> attendants = new ArrayList<>();

    public List<Attendant> list() {
        return Collections.unmodifiableList(attendants);
    }

    public void create(Attendant attendant) {
        attendants.add(attendant);
    }

}
