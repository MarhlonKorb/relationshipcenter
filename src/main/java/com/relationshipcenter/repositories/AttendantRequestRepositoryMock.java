package com.relationshipcenter.repositories;

import com.relationshipcenter.models.AttendantRequest;
import com.relationshipcenter.shared.RepositoryGen;
import com.relationshipcenter.shared.RepositoryGenUpdate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Repository
public class AttendantRequestRepositoryMock implements RepositoryGen<AttendantRequest, Long>, RepositoryGenUpdate<AttendantRequest> {

    private final List<AttendantRequest> attendantRequests = new LinkedList<>();

    @Override
    public void create(AttendantRequest attendantRequest) {
        attendantRequests.add(attendantRequest);
    }

    @Override
    public Collection<AttendantRequest> list() {
        return Collections.unmodifiableCollection(attendantRequests);
    }

    public void remove(AttendantRequest attendantRequest) {
        attendantRequests.remove(attendantRequest);
    }

}
