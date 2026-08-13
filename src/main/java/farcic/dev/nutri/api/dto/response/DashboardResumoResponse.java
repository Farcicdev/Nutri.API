package farcic.dev.nutri.api.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record DashboardResumoResponse(

        long totalPacientes,
        long consultasHoje,
        long consultasConfirmadasHoje,
        long consultasProximosSeteDias,
        long avaliacoesRealizadas,
        long avaliacoesRealizadasNoMes,
        List<AtendimentosPorMesResponse> atendimentosPorMes,
        List<ProximaConsultaResponse> proximasConsultas,
        List<PacienteRecenteResponse> pacientesRecentes
) {
}
