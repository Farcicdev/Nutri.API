package farcic.dev.nutri.api.dto.response;

import farcic.dev.nutri.api.entity.enums.Sexo;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PacienteResponse(
        Long id,
        String nome,
        LocalDate dataNascimento,
        Sexo sexo,
        String email,
        String telefone,
        String observacoes,
        boolean ativo,
        Long nutricionistaId,
        String nutricionistaNome
) {
}
