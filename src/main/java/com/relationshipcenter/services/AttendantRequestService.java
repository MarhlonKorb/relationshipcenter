package com.relationshipcenter.services;

import com.relationshipcenter.exception.AttendantNotAvaliableException;
import com.relationshipcenter.models.Attendant;
import com.relationshipcenter.models.AttendantRequest;
import com.relationshipcenter.shared.RepositoryGen;
import com.relationshipcenter.shared.RepositoryGenUpdate;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AttendantRequestService {

    private static final Logger logger = Logger.getLogger(AttendantRequestService.class.getName());

    private final RepositoryGen<AttendantRequest, Long> attendantRequestRepository;
    private final RepositoryGenUpdate<AttendantRequest> attendantRequestRepositoryGenUpdate;

    public AttendantRequestService(
            RepositoryGen<AttendantRequest, Long> attendantRequestRepository,
            RepositoryGenUpdate<AttendantRequest> attendantRequestRepositoryGenUpdate
    ) {
        this.attendantRequestRepository = attendantRequestRepository;
        this.attendantRequestRepositoryGenUpdate = attendantRequestRepositoryGenUpdate;
    }

    public void create(AttendantRequest attendantRequest) {
        if (!isAvaliable(attendantRequest.attendant())) {
            throw new AttendantNotAvaliableException();
        }
        attendantRequestRepository.create(attendantRequest);
    }

    public boolean isAvaliable(Attendant attendant) {
        return attendantRequestRepository.list().stream()
                .filter(attendantRequest -> attendantRequest.attendant().id().equals(attendant.id())).toList().size() < 3;
    }

    public long countByAttendant(Attendant attendant) {
        return attendantRequestRepository.list().stream()
                .filter(attendantRequest -> attendantRequest
                        .attendant()
                        .id().equals(attendant.id()))
                .toList()
                .size();
    }

    public void remove(Attendant attendant) {
        var attendantRequests = attendantRequestRepository.list();
        if (!attendantRequests.isEmpty()) {
            var finishedRequestOpt = attendantRequests.stream()
                    .filter(attendantRequest -> attendantRequest.attendant().id().equals(attendant.id()))
                    .findFirst();

            finishedRequestOpt.ifPresent(finishedAttendantRequest -> {
                attendantRequestRepositoryGenUpdate.remove(finishedAttendantRequest);
                logger.info(" [" + finishedAttendantRequest.requestSubjectSubscription() + "] - Finalized request " + finishedAttendantRequest.request().id());
            });
        }
    }
}
