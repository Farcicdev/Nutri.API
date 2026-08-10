package farcic.dev.nutri.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record NutricionistaSenhaRequest(
        @NotBlank @Size(min = 8, max = 100)
        String senha
) {
}
