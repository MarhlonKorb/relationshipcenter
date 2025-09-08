package com.relationshipcenter.config;

import com.relationshipcenter.models.*;
import com.relationshipcenter.repositories.AttendanceTypeSubjectRepositoryMock;
import com.relationshipcenter.repositories.AttendanceTypeRepositoryMock;
import com.relationshipcenter.repositories.AttendantRepositoryMock;
import com.relationshipcenter.repositories.SubjectRepositoryMock;
import com.relationshipcenter.services.TeamService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartRunner implements CommandLineRunner {

    private final TeamService teamService;

    private final AttendanceTypeRepositoryMock attendanceTypeRepositoryMock;

    private final SubjectRepositoryMock subjectRepositoryMock;

    private final AttendanceTypeSubjectRepositoryMock attendanceTypeSubjectRepositoryMock;

    private final AttendantRepositoryMock attendantRepositoryMock;

    public StartRunner(
            TeamService teamService,
            AttendanceTypeRepositoryMock attendanceTypeRepositoryMock,
            SubjectRepositoryMock subjectRepositoryMock,
            AttendanceTypeSubjectRepositoryMock attendanceTypeSubjectRepositoryMock,
            AttendantRepositoryMock attendantRepositoryMock
    ) {
        this.teamService = teamService;
        this.attendanceTypeRepositoryMock = attendanceTypeRepositoryMock;
        this.subjectRepositoryMock = subjectRepositoryMock;
        this.attendanceTypeSubjectRepositoryMock = attendanceTypeSubjectRepositoryMock;
        this.attendantRepositoryMock = attendantRepositoryMock;
    }

    @Override
    public void run(String... args) throws Exception {
        var attandenceTypeCartoes = new AttendanceType("Cartões");
        var attandenceTypeEmprestimos = new AttendanceType("Empréstimos");
        var attandenceTypeOutrosAssuntos = new AttendanceType("Outros Assuntos");

        attendanceTypeRepositoryMock.create(attandenceTypeCartoes);
        attendanceTypeRepositoryMock.create(attandenceTypeEmprestimos);
        attendanceTypeRepositoryMock.create(attandenceTypeOutrosAssuntos);

        var subjectProblemasCartao = new Subject("Problemas com cartão");
        var subjectContratacaoEmprestimo = new Subject("Contratação de empréstimo");
        var subjectOutrosAssuntos = new Subject("Outros Assuntos");

        subjectRepositoryMock.create(subjectProblemasCartao);
        subjectRepositoryMock.create(subjectContratacaoEmprestimo);
        subjectRepositoryMock.create(subjectOutrosAssuntos);

        var teamCartoes = new Team().setAssistanceType(attandenceTypeCartoes);
        var teamEmprestimos = new Team().setAssistanceType(attandenceTypeEmprestimos);
        var teamOutrosAssuntos = new Team().setAssistanceType(attandenceTypeOutrosAssuntos).setDefault(true);

        attendanceTypeSubjectRepositoryMock.create(new AttendanceTypeSubject(attandenceTypeCartoes, subjectProblemasCartao));
        attendanceTypeSubjectRepositoryMock.create(new AttendanceTypeSubject(attandenceTypeEmprestimos, subjectContratacaoEmprestimo));
        attendanceTypeSubjectRepositoryMock.create(new AttendanceTypeSubject(attandenceTypeOutrosAssuntos, subjectOutrosAssuntos));

        teamService.addTeam(teamCartoes);
        teamService.addTeam(teamEmprestimos);
        teamService.addTeam(teamOutrosAssuntos);

        attendantRepositoryMock.create(new Attendant("Atendente cartões 1", teamCartoes));
        attendantRepositoryMock.create(new Attendant("Atendente empréstimos 1", teamEmprestimos));
        attendantRepositoryMock.create(new Attendant("Atendente outros assuntos 1", teamOutrosAssuntos));
    }
}
