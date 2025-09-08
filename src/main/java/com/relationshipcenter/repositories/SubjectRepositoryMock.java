package com.relationshipcenter.repositories;

import com.relationshipcenter.models.Subject;
import com.relationshipcenter.shared.RepositoryGen;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Repository
public class SubjectRepositoryMock implements RepositoryGen<Subject, Long> {

    private final Set<Subject> subjects = new HashSet<>();

    @Override
    public void create(Subject subject) {
        subjects.add(subject);
    }

    @Override
    public Collection<Subject> list() {
        return Set.copyOf(subjects);
    }
}
