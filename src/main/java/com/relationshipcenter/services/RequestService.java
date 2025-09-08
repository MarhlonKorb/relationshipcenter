package com.relationshipcenter.services;

import com.relationshipcenter.models.AttendanceType;
import com.relationshipcenter.models.AttendanceTypeSubject;
import com.relationshipcenter.models.Request;
import com.relationshipcenter.models.Team;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

@Service
public class RequestService {
    private static final Logger logger = Logger.getLogger(RequestService.class.getName());

    private final Map<AttendanceType, Queue<Request>> requestsQueues = new ConcurrentHashMap<>();

    private final TeamService teamService;

    private final AttendanceTypeSubjectService attendanceTypeSubjectService;

    public RequestService(
            TeamService teamService,
            AttendanceTypeSubjectService attendanceTypeSubjectService
    ) {
        this.teamService = teamService;
        this.attendanceTypeSubjectService = attendanceTypeSubjectService;
    }

    public void enqueue(Request request) {
        // Descobre o tipo do atendimento pelo subject
        var attendanceTypeSubjectOpt = attendanceTypeSubjectService.list().stream()
                .filter(ats -> ats.acceptedSubject().equals(request.subject()))
                .findFirst();

        AttendanceType type = attendanceTypeSubjectOpt
                .map(AttendanceTypeSubject::attendanceType)
                .orElseGet(() -> teamService.teams().stream()
                        .filter(Team::isDefault)
                        .map(Team::attendanceType)
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Nenhum team default configurado")));

        // Coloca na fila do tipo
        enqueuePerType(request, type);

        logger.info("New request added: " + request.subject() + " [" + type.name() + "]");
    }

    private void enqueuePerType(Request request, AttendanceType type) {
        requestsQueues
                .computeIfAbsent(type, t -> new ConcurrentLinkedDeque<>())
                .add(request);
    }

    public Map<AttendanceType, Queue<Request>> allQueues() {
        return requestsQueues;
    }
}
