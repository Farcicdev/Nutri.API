package farcic.dev.nutri.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record NutricionistaRequest(
        @NotBlank @Size(max = 120)
        String nome,
        @NotBlank @Email @Size(max = 160)
        String email,
        @NotBlank @Size(min = 8, max = 100)
        String senha,
        @Size(max = 20)
        String telefone,
        @NotBlank @Size(max = 30)
        String crn,
        @Size(max = 120)
        String especialidade
) {
}
