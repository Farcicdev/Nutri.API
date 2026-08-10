package farcic.dev.nutri.api.dto.request;

import farcic.dev.nutri.api.entity.enums.StatusConsulta;
import jakarta.validation.constraints.NotNull;

public record ConsultaStatusRequest(
        @NotNull StatusConsulta status
) {
}
