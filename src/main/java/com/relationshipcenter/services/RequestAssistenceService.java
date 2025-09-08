package com.relationshipcenter.services;

import com.relationshipcenter.models.*;
import com.relationshipcenter.shared.RepositoryGen;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class RequestAssistenceService {

    private static final Logger logger = Logger.getLogger(RequestAssistenceService.class.getName());

    private final RequestService requestService;

    private final TeamService teamService;

    private final RepositoryGen<Attendant, Long> attendantRepository;

    private final AttendantRequestService attendantRequestService;

    public RequestAssistenceService(
            RequestService requestService,
            TeamService teamService,
            RepositoryGen<Attendant, Long> attendantRepository,
            AttendantRequestService attendantRequestService
    ) {
        this.requestService = requestService;
        this.teamService = teamService;
        this.attendantRepository = attendantRepository;
        this.attendantRequestService = attendantRequestService;
    }

    public void distributeRequest() {
        requestService.allQueues().forEach((type, queue) -> {
            Request request;
            while ((request = queue.peek()) != null) {
                var attendantOpt = findAvailableAttendantFor(request, type);

                if (attendantOpt.isEmpty()) {
                    logger.info("No attendant available for type " + type + ". Request continua na fila.");
                    break;
                }
                Request finalRequest = request;
                attendantOpt.ifPresent(attendant -> {
                    logger.info("Consuming message for attendant: " + attendant.name() +
                            ", Team: " + attendant.team().attendanceType().name());
                    attendantRequestService.create(new AttendantRequest(attendant, finalRequest));
                    queue.poll();
                });
            }
        });
    }

    private Optional<Attendant> findAvailableAttendantFor(Request request, AttendanceType type) {
        // Busca apenas times com o AttendanceType da fila
        return teamService.teams().stream()
                .filter(team -> team.attendanceType().equals(type))
                .findFirst()
                .flatMap(team -> teamService.findAvailableAttendant(team, request.subject()));
    }

    public void finalizeRequest() {
        logger.info("Finalizing older requests");
        Map<Team, List<Attendant>> attendantsByTeam = attendantRepository.list().stream()
                .collect(Collectors.groupingBy(Attendant::team));

        teamService.teams().forEach(team -> {
            new ArrayList<>(attendantsByTeam.getOrDefault(team, List.of()))
                    .forEach(attendantRequestService::remove);
        });
    }


}
