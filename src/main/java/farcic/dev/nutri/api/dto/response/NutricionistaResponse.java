package farcic.dev.nutri.api.dto.response;

import lombok.Builder;

@Builder
public record NutricionistaResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        String crn,
        String especialidade,
        boolean ativo
) {
}
