package com.relationshipcenter.repositories;

import com.relationshipcenter.models.AttendanceType;
import com.relationshipcenter.shared.RepositoryGen;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class AttendanceTypeRepositoryMock implements RepositoryGen<AttendanceType, Long> {

    private final List<AttendanceType> attendanceTypes = new ArrayList<>();

    public List<AttendanceType> list() {
        return Collections.unmodifiableList(attendanceTypes);
    }

    public void create(AttendanceType attendant) {
        attendanceTypes.add(attendant);
    }

}
