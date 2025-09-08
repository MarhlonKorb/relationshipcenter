package com.relationshipcenter.repositories;

import com.relationshipcenter.models.AttendanceTypeSubject;
import com.relationshipcenter.shared.RepositoryGen;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Repository
public class AttendanceTypeSubjectRepositoryMock implements RepositoryGen<AttendanceTypeSubject, Long> {

    private final Set<AttendanceTypeSubject> attendanceTypeSubjects = new HashSet<>();

    @Override
    public void create(AttendanceTypeSubject model) {
        attendanceTypeSubjects.add(model);
    }

    @Override
    public Collection<AttendanceTypeSubject> list() {
        return Set.copyOf(attendanceTypeSubjects);
    }

}
