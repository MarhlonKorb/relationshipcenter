package com.relationshipcenter;

import com.relationshipcenter.models.*;
import com.relationshipcenter.repositories.AttendanceTypeSubjectRepositoryMock;
import com.relationshipcenter.repositories.AttendantRepositoryMock;
import com.relationshipcenter.repositories.AttendantRequestRepositoryMock;
import com.relationshipcenter.repositories.TeamRepositoryMock;
import com.relationshipcenter.services.*;
import com.relationshipcenter.shared.RepositoryGen;
import com.relationshipcenter.shared.RepositoryGenUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("unitTest")
public class RequestAssistenceServiceTest {

    private RequestAssistenceService requestAssistenceService;
    private TeamService teamService;
    private RequestService requestService;
    private RepositoryGen<Team, Long> teamRepository;
    private RepositoryGen<Attendant, Long> attendantRepository;
    private RepositoryGenUpdate<AttendantRequest> attendantRequestRepositoryGenUpdate;
    private AttendanceTypeSubjectRepositoryMock attendanceTypeSubjectRepositoryMock;
    private AttendantRequestRepositoryMock attendantRequestRepositoryMock;
    private AttendanceTypeSubjectService attendanceTypeSubjectService;
    private AttendantRequestService attendantRequestService;

    @BeforeEach
    void setUp() {
        attendantRepository = new AttendantRepositoryMock();
        teamRepository = new TeamRepositoryMock();
        attendanceTypeSubjectRepositoryMock = new AttendanceTypeSubjectRepositoryMock();
        attendanceTypeSubjectService = new AttendanceTypeSubjectService(attendanceTypeSubjectRepositoryMock);
        attendantRequestRepositoryMock = new AttendantRequestRepositoryMock();
        attendantRequestRepositoryGenUpdate = attendantRequestRepositoryMock;
        attendantRequestService= new AttendantRequestService(attendantRequestRepositoryMock, attendantRequestRepositoryGenUpdate);
        teamService = new TeamService(teamRepository, attendantRepository, attendanceTypeSubjectService, attendantRequestService);
        requestService = new RequestService(teamService, attendanceTypeSubjectService);
        requestAssistenceService = new RequestAssistenceService(requestService, teamService, attendantRepository, attendantRequestService);
    }

    @Test
    @DisplayName("Deve distribuir requests corretamente entre atendentes disponíveis")
    void testDistribution() {
        var cartaoType = new AttendanceType().setName("Cartão");
        var subject1 = new Subject("Req 1");
        var subject2 = new Subject("Req 2");

        attendanceTypeSubjectRepositoryMock.create(new AttendanceTypeSubject(cartaoType, subject1));
        attendanceTypeSubjectRepositoryMock.create(new AttendanceTypeSubject(cartaoType, subject2));

        var cartaoTeam = new Team().setAssistanceType(cartaoType);

        var attendant1 = new Attendant("João", cartaoTeam);
        var attendant2 = new Attendant("Maria", cartaoTeam);

        attendantRepository.create(attendant1);
        attendantRepository.create(attendant2);
        teamService.addTeam(cartaoTeam);

        requestService.enqueue(new Request(subject1));
        requestService.enqueue(new Request(subject2));

        requestAssistenceService.distributeRequest();

        // valida que as requests foram atribuídas via repository
        assertEquals(1, attendantRequestService.countByAttendant(attendant1));
        assertEquals(1, attendantRequestService.countByAttendant(attendant2));

        // valida que a fila do tipo "Cartão" está vazia
        var queue = requestService.allQueues().get(cartaoType);
        assertTrue(queue == null || queue.isEmpty(), "A fila do Cartão deve estar vazia");
    }

    @Test
    @DisplayName("Request permanece na fila se atendentes estiverem cheios")
    void testRequestCannotBeConsumedWhenAttendantsFull() {
        var cartaoType = new AttendanceType().setName("Cartão");
        var subject = new Subject("Req A");
        attendanceTypeSubjectRepositoryMock.create(new AttendanceTypeSubject(cartaoType, subject));

        var cartaoTeam = new Team().setAssistanceType(cartaoType);
        var attendant1 = new Attendant("João", cartaoTeam);
        var attendant2 = new Attendant("Maria", cartaoTeam);

        attendantRepository.create(attendant1);
        attendantRepository.create(attendant2);
        teamService.addTeam(cartaoTeam);

        // preenche atendentes com 3 requests cada
        for (int i = 1; i <= 3; i++) {
            attendantRequestRepositoryMock.create(new AttendantRequest(attendant1, new Request(new Subject("Req " + i))));
            attendantRequestRepositoryMock.create(new AttendantRequest(attendant2, new Request(new Subject("Req " + i))));
        }

        var newRequest = new Request(subject);
        requestService.enqueue(newRequest);

        requestAssistenceService.distributeRequest();

        assertEquals(3, attendantRequestService.countByAttendant(attendant1));
        assertEquals(3, attendantRequestService.countByAttendant(attendant2));
        assertEquals(1, requestService.allQueues().size());
    }

    @Test
    @DisplayName("Request vai para time default se não houver atendente disponível no time original")
    void testRequestFallbackToDefaultTeam() {
        var defaultType = new AttendanceType().setName("Default Team");
        var subjectDefault = new Subject("Req Default");
        attendanceTypeSubjectRepositoryMock.create(new AttendanceTypeSubject(defaultType, subjectDefault));

        var defaultTeam = new Team().setAssistanceType(defaultType).setDefault(true);
        var defaultAttendant = new Attendant("Default Attendant", defaultTeam);
        attendantRepository.create(defaultAttendant);
        teamService.addTeam(defaultTeam);

        var cartaoType = new AttendanceType().setName("Cartão");
        var subjectCartao = new Subject("Req Cartão");
        attendanceTypeSubjectRepositoryMock.create(new AttendanceTypeSubject(cartaoType, subjectCartao));

        var cartaoTeam = new Team().setAssistanceType(cartaoType);
        var busyAttendant = new Attendant("João", cartaoTeam);
        attendantRepository.create(busyAttendant);
        teamService.addTeam(cartaoTeam);

        // preenche atendente do time original
        for (int i = 1; i <= 3; i++) {
            attendantRequestRepositoryMock.create(new AttendantRequest(busyAttendant, new Request(new Subject("Req " + i))));
        }

        var request = new Request(subjectDefault);
        requestService.enqueue(request);

        requestAssistenceService.distributeRequest();

        // valida que o default attendant recebeu a request
        assertEquals(1, attendantRequestService.countByAttendant(defaultAttendant));

        // valida que a fila do tipo default está vazia
        var queue = requestService.allQueues().get(defaultType);
        assertTrue(queue == null || queue.isEmpty(), "A fila do Default Team deve estar vazia");
    }

    @Test
    @DisplayName("Solicitação permanece na fila se time default também estiver cheio")
    void testRequestRemainsInQueueWhenDefaultTeamFull() {
        var defaultType = new AttendanceType().setName("Default Team");
        var subjectPending = new Subject("Req Pending");
        attendanceTypeSubjectRepositoryMock.create(new AttendanceTypeSubject(defaultType, subjectPending));

        var defaultTeam = new Team().setAssistanceType(defaultType).setDefault(true);
        var defaultAttendant = new Attendant("Default Attendant", defaultTeam);
        attendantRepository.create(defaultAttendant);
        teamService.addTeam(defaultTeam);

        for (int i = 1; i <= 3; i++) {
            attendantRequestRepositoryMock.create(new AttendantRequest(defaultAttendant, new Request(new Subject("Req " + i))));
        }

        var cartaoType = new AttendanceType().setName("Cartão");
        var subjectCartao = new Subject("Req Cartão");
        attendanceTypeSubjectRepositoryMock.create(new AttendanceTypeSubject(cartaoType, subjectCartao));

        var cartaoTeam = new Team().setAssistanceType(cartaoType);
        var attendant = new Attendant("João", cartaoTeam);
        attendantRepository.create(attendant);
        teamService.addTeam(cartaoTeam);

        for (int i = 1; i <= 3; i++) {
            attendantRequestRepositoryMock.create(new AttendantRequest(attendant, new Request(new Subject("Req " + i))));
        }

        var request = new Request(subjectPending);
        requestService.enqueue(request);

        requestAssistenceService.distributeRequest();

        assertEquals(1, requestService.allQueues().size());
        assertEquals(3, attendantRequestService.countByAttendant(defaultAttendant));
    }
}
