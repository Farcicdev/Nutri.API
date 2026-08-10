package farcic.dev.nutri.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import farcic.dev.nutri.api.entity.enums.TipoConsulta;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ConsultaRequest(
        @NotNull @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime dataConsulta,
        @NotNull
        TipoConsulta tipo,
        @Size(max = 2000)
        String observacoes,
        @NotNull @Positive
        Long pacienteId,
        @NotNull @Positive
        Long nutricionistaId
) {
}
