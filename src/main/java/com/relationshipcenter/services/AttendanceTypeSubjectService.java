package com.relationshipcenter.services;

import com.relationshipcenter.models.AttendanceType;
import com.relationshipcenter.models.AttendanceTypeSubject;
import com.relationshipcenter.shared.RepositoryGen;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AttendanceTypeSubjectService {

    private final RepositoryGen<AttendanceTypeSubject, Long> AttendanceTypeSubjectRepository;

    public AttendanceTypeSubjectService(RepositoryGen<AttendanceTypeSubject, Long> AttendanceTypeSubjectRepository) {
        this.AttendanceTypeSubjectRepository = AttendanceTypeSubjectRepository;
    }

    public Collection<AttendanceTypeSubject> findByAttendanceType(AttendanceType attendanceType) {
        return AttendanceTypeSubjectRepository.list().stream()
                .filter(ats -> ats.attendanceType().id().equals(attendanceType.id()))
                .toList();
    }

    public Collection<AttendanceTypeSubject> list() {
        return AttendanceTypeSubjectRepository.list();
    }
}
