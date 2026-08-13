package farcic.dev.nutri.api.dto.response;

import farcic.dev.nutri.api.entity.enums.StatusConsulta;
import farcic.dev.nutri.api.entity.enums.TipoConsulta;

import java.time.LocalDateTime;

public record ProximaConsultaResponse(
        Long id,
        LocalDateTime dataConsulta,
        Long pacienteId,
        String pacienteNome,
        TipoConsulta tipo,
        StatusConsulta status

) {
}
