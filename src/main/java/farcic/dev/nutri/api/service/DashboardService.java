package farcic.dev.nutri.api.service;

import farcic.dev.nutri.api.dto.response.AtendimentosPorMesResponse;
import farcic.dev.nutri.api.dto.response.DashboardResumoResponse;
import farcic.dev.nutri.api.dto.response.PacienteRecenteResponse;
import farcic.dev.nutri.api.dto.response.ProximaConsultaResponse;
import farcic.dev.nutri.api.entity.enums.StatusConsulta;
import farcic.dev.nutri.api.repository.AvaliacaoAntropometricaRepository;
import farcic.dev.nutri.api.repository.ConsultaRepository;
import farcic.dev.nutri.api.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final int LIMITE_PROXIMAS_CONSULTAS = 5;
    private static final int LIMITE_PACIENTES_RECENTES = 4;

    private final PacienteRepository pacienteRepository;
    private final ConsultaRepository consultaRepository;
    private final AvaliacaoAntropometricaRepository avaliacaoRepository;
    private final NutricionistaAutenticadoService nutricionistaAutenticadoService;

    @Transactional(readOnly = true)
    public DashboardResumoResponse obterResumo() {
        Long nutricionistaId = nutricionistaAutenticadoService.getId();
        LocalDate hoje = LocalDate.now();
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime inicioHoje = hoje.atStartOfDay();
        LocalDateTime inicioAmanha = hoje.plusDays(1).atStartOfDay();
        LocalDateTime fimExclusivoProximosSeteDias = hoje.plusDays(7).atStartOfDay();
        LocalDateTime inicioMes = hoje.withDayOfMonth(1).atStartOfDay();
        LocalDateTime inicioProximoMes = hoje.plusMonths(1).withDayOfMonth(1).atStartOfDay();

        long totalPacientes = pacienteRepository
                .countByNutricionistaIdAndAtivoTrue(nutricionistaId);

        long consultasHoje = consultaRepository
                .countByNutricionistaIdAndStatusNotAndDataConsultaGreaterThanEqualAndDataConsultaLessThan(
                        nutricionistaId,
                        StatusConsulta.CANCELADA,
                        inicioHoje,
                        inicioAmanha
                );

        long consultasConfirmadasHoje = consultaRepository
                .countByNutricionistaIdAndStatusAndDataConsultaGreaterThanEqualAndDataConsultaLessThan(
                        nutricionistaId,
                        StatusConsulta.AGENDADA,
                        inicioHoje,
                        inicioAmanha
                );

        long consultasProximosSeteDias = consultaRepository
                .countByNutricionistaIdAndStatusNotAndDataConsultaGreaterThanEqualAndDataConsultaLessThan(
                        nutricionistaId,
                        StatusConsulta.CANCELADA,
                        inicioHoje,
                        fimExclusivoProximosSeteDias
                );

        long avaliacoesRealizadas = avaliacaoRepository
                .countByConsultaNutricionistaId(nutricionistaId);

        long avaliacoesRealizadasNoMes = avaliacaoRepository
                .countByConsultaNutricionistaIdAndDataAvaliacaoGreaterThanEqualAndDataAvaliacaoLessThan(
                        nutricionistaId,
                        inicioMes,
                        inicioProximoMes
                );

        return DashboardResumoResponse.builder()
                .totalPacientes(totalPacientes)
                .consultasHoje(consultasHoje)
                .consultasConfirmadasHoje(consultasConfirmadasHoje)
                .consultasProximosSeteDias(consultasProximosSeteDias)
                .avaliacoesRealizadas(avaliacoesRealizadas)
                .avaliacoesRealizadasNoMes(avaliacoesRealizadasNoMes)
                .atendimentosPorMes(buscarAtendimentosPorMes(nutricionistaId, hoje))
                .proximasConsultas(buscarProximasConsultas(nutricionistaId, agora))
                .pacientesRecentes(buscarPacientesRecentes(nutricionistaId, agora))
                .build();
    }

    private List<AtendimentosPorMesResponse> buscarAtendimentosPorMes(
            Long nutricionistaId,
            LocalDate hoje
    ) {
        int ano = hoje.getYear();
        LocalDateTime inicioAno = LocalDate.of(ano, 1, 1).atStartOfDay();
        LocalDateTime inicioProximoAno = LocalDate.of(ano + 1, 1, 1).atStartOfDay();

        Map<Integer, AtendimentosPorMesResponse> atendimentosPorMes = consultaRepository
                .contarAtendimentosPorMes(
                        nutricionistaId,
                        StatusConsulta.REALIZADA,
                        inicioAno,
                        inicioProximoAno
                )
                .stream()
                .collect(Collectors.toMap(AtendimentosPorMesResponse::mes, Function.identity()));

        return IntStream.rangeClosed(1, 12)
                .mapToObj(mes -> atendimentosPorMes.getOrDefault(
                        mes,
                        new AtendimentosPorMesResponse(ano, mes, 0)
                ))
                .toList();
    }

    private List<ProximaConsultaResponse> buscarProximasConsultas(
            Long nutricionistaId,
            LocalDateTime agora
    ) {
        return consultaRepository.buscarProximasConsultas(
                        nutricionistaId,
                        StatusConsulta.CANCELADA,
                        agora,
                        PageRequest.of(0, LIMITE_PROXIMAS_CONSULTAS)
                )
                .stream()
                .map(consulta -> new ProximaConsultaResponse(
                        consulta.getId(),
                        consulta.getDataConsulta(),
                        consulta.getPaciente().getId(),
                        consulta.getPaciente().getNome(),
                        consulta.getTipo(),
                        consulta.getStatus()
                ))
                .toList();
    }

    private List<PacienteRecenteResponse> buscarPacientesRecentes(
            Long nutricionistaId,
            LocalDateTime agora
    ) {
        return consultaRepository.buscarUltimaConsultaDosPacientes(
                        nutricionistaId,
                        agora,
                        PageRequest.of(0, LIMITE_PACIENTES_RECENTES)
                )
                .stream()
                .map(consulta -> {
                    var paciente = consulta.getPaciente();
                    var avaliacao = avaliacaoRepository
                            .findFirstByConsultaPacienteIdAndConsultaNutricionistaIdOrderByDataAvaliacaoDesc(
                                    paciente.getId(),
                                    nutricionistaId
                            )
                            .orElse(null);

                    return new PacienteRecenteResponse(
                            paciente.getId(),
                            paciente.getNome(),
                            consulta.getDataConsulta(),
                            avaliacao == null ? null : avaliacao.getImc(),
                            avaliacao == null ? null : avaliacao.getPercentualGordura()
                    );
                })
                .toList();
    }
}
