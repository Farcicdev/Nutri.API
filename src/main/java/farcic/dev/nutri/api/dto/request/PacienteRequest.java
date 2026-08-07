package farcic.dev.nutri.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import farcic.dev.nutri.api.entity.enums.Sexo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PacienteRequest(
        @NotBlank @Size(max = 120)
        String nome,
        @NotNull @Past
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,
        @NotNull
        Sexo sexo,
        @NotBlank @Email @Size(max = 160)
        String email,
        @Size(max = 20)
        String telefone,
        @Size(max = 2000)
        String observacoes,
        @NotNull @Positive
        Long nutricionistaId
) {
}
