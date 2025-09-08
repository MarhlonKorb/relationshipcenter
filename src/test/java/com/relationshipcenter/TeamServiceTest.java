package com.relationshipcenter;

import com.relationshipcenter.exception.DefaultTeamAlreadyDefinedException;
import com.relationshipcenter.models.AttendanceType;
import com.relationshipcenter.models.Attendant;
import com.relationshipcenter.models.AttendantRequest;
import com.relationshipcenter.models.Team;
import com.relationshipcenter.repositories.AttendanceTypeSubjectRepositoryMock;
import com.relationshipcenter.repositories.AttendantRepositoryMock;
import com.relationshipcenter.repositories.AttendantRequestRepositoryMock;
import com.relationshipcenter.repositories.TeamRepositoryMock;
import com.relationshipcenter.services.AttendanceTypeSubjectService;
import com.relationshipcenter.services.AttendantRequestService;
import com.relationshipcenter.services.TeamService;
import com.relationshipcenter.shared.RepositoryGen;
import com.relationshipcenter.shared.RepositoryGenUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamServiceTest {

    private TeamService teamService;

    private RepositoryGen<Team, Long> teamRepository;

    private RepositoryGen<Attendant, Long> attendantRepository;
    private RepositoryGenUpdate<AttendantRequest> attendantRequestRepositoryGenUpdate;

    private AttendanceTypeSubjectRepositoryMock attendanceTypeSubjectRepositoryMock;
    private AttendanceTypeSubjectService attendanceTypeSubjectService;

    private AttendantRequestRepositoryMock attendantRequestRepositoryMock;
    private AttendantRequestService attendantRequestService;

    @BeforeEach
    void setUp() {
        attendantRepository = new AttendantRepositoryMock();
        teamRepository = new TeamRepositoryMock();
        attendanceTypeSubjectRepositoryMock = new AttendanceTypeSubjectRepositoryMock();
        attendanceTypeSubjectService = new AttendanceTypeSubjectService(attendanceTypeSubjectRepositoryMock);
        attendantRequestRepositoryMock = new AttendantRequestRepositoryMock();
        attendantRequestRepositoryGenUpdate = attendantRequestRepositoryMock;
        attendantRequestService = new AttendantRequestService(attendantRequestRepositoryMock, attendantRequestRepositoryGenUpdate);
        teamService = new TeamService(teamRepository, attendantRepository, attendanceTypeSubjectService, attendantRequestService);
    }

    @Test
    @DisplayName("Deve permitir adicionar um time padrão quando não houver um padrão adicionado")
    void addTeamDefault() {
        var team = new Team().setAssistanceType(new AttendanceType("assistenceType1")).setDefault(true);
        assertDoesNotThrow(() -> teamService.addTeam(team));
        assertEquals(teamRepository.list().size(), 1);
    }

    @Test
    @DisplayName("Não deve permitir adicionar um time padrão quando houver um time padrão adicionado")
    void addTeam() {
        var team1 = new Team().setAssistanceType(new AttendanceType("assistenceType1")).setDefault(true);
        teamService.addTeam(team1);
        var team2 = new Team().setAssistanceType(new AttendanceType("assistenceType1")).setDefault(true);
        assertThrows(DefaultTeamAlreadyDefinedException.class, () -> teamService.addTeam(team2));
        assertEquals(teamRepository.list().size(), 1);
    }
}