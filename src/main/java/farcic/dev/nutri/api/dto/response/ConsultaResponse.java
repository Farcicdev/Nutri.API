package farcic.dev.nutri.api.dto.response;

import farcic.dev.nutri.api.entity.enums.StatusConsulta;
import farcic.dev.nutri.api.entity.enums.TipoConsulta;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ConsultaResponse(
        Long id,
        LocalDateTime dataConsulta,
        StatusConsulta status,
        TipoConsulta tipo,
        String observacoes,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm,
        Long pacienteId,
        String pacienteNome,
        Long nutricionistaId,
        String nutricionistaNome
) {
}
