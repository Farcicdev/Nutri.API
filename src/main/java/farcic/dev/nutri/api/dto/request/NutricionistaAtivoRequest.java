package farcic.dev.nutri.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record NutricionistaAtivoRequest(
        @NotNull Boolean ativo
) {
}
