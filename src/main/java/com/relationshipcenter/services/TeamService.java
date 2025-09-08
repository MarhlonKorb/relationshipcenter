package com.relationshipcenter.services;

import com.relationshipcenter.exception.DefaultTeamAlreadyDefinedException;
import com.relationshipcenter.models.Attendant;
import com.relationshipcenter.models.Subject;
import com.relationshipcenter.models.Team;
import com.relationshipcenter.shared.RepositoryGen;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

@Service
public class TeamService {

    private final RepositoryGen<Team, Long> teamRepository;

    private final RepositoryGen<Attendant, Long> attendantRepository;

    private final AttendanceTypeSubjectService attendanceTypeSubjectService;

    private final AttendantRequestService attendantRequestService;

    public TeamService(
            RepositoryGen<Team, Long> teamRepository,
            RepositoryGen<Attendant, Long> attendantRepository,
            AttendanceTypeSubjectService attendanceTypeSubjectService,
            AttendantRequestService attendantRequestService
    ) {
        this.teamRepository = teamRepository;
        this.attendantRepository = attendantRepository;
        this.attendanceTypeSubjectService = attendanceTypeSubjectService;
        this.attendantRequestService = attendantRequestService;
    }

    public void addTeam(Team team) {
        if (team.isDefault()) {
            var hasDefault = teamRepository.list().stream().filter(Team::isDefault).findAny();
            if (hasDefault.isPresent()) {
                throw new DefaultTeamAlreadyDefinedException("Default team already defined!");
            }
        }
        teamRepository.create(team);
    }

    public Optional<Attendant> findAvailableAttendant(Team team, Subject subject) {
        var subjects = attendanceTypeSubjectService.findByAttendanceType(team.attendanceType());
        if (subjects.isEmpty()) {
            return Optional.empty();
        }

        // Se não for default e o subject não for aceito, retorna vazio
        if (!team.isDefault() && subjects.stream().noneMatch(ats -> ats.acceptedSubject().equals(subject))) {
            return Optional.empty();
        }

        // Retorna o atendente com menos requests e menos de 3 atribuições
        return attendantRepository.list().stream()
                .filter(a -> a.team().equals(team))
                .filter(attendantRequestService::isAvaliable)
                .min(Comparator.comparingInt(a -> Math.toIntExact(attendantRequestService.countByAttendant(a))));
    }

    public Set<Team> teams() {
        return Set.copyOf(teamRepository.list());
    }

}
