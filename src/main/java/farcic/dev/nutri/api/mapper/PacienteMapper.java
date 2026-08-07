package farcic.dev.nutri.api.mapper;

import farcic.dev.nutri.api.dto.request.PacienteRequest;
import farcic.dev.nutri.api.dto.response.PacienteResponse;
import farcic.dev.nutri.api.entity.Nutricionista;
import farcic.dev.nutri.api.entity.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

    public Paciente toEntity(PacienteRequest request, Nutricionista nutricionista) {
        return Paciente.builder()
                .nome(request.nome().trim())
                .dataNascimento(request.dataNascimento())
                .sexo(request.sexo())
                .email(request.email().trim().toLowerCase())
                .telefone(normalizarOpcional(request.telefone()))
                .observacoes(normalizarOpcional(request.observacoes()))
                .nutricionista(nutricionista)
                .build();
    }

    public void updateEntity(
            Paciente paciente,
            PacienteRequest request,
            Nutricionista nutricionista
    ) {
        paciente.setNome(request.nome().trim());
        paciente.setDataNascimento(request.dataNascimento());
        paciente.setSexo(request.sexo());
        paciente.setEmail(request.email().trim().toLowerCase());
        paciente.setTelefone(normalizarOpcional(request.telefone()));
        paciente.setObservacoes(normalizarOpcional(request.observacoes()));
        paciente.setNutricionista(nutricionista);
    }

    public PacienteResponse toResponse(Paciente paciente) {
        return PacienteResponse.builder()
                .id(paciente.getId())
                .nome(paciente.getNome())
                .dataNascimento(paciente.getDataNascimento())
                .sexo(paciente.getSexo())
                .email(paciente.getEmail())
                .telefone(paciente.getTelefone())
                .observacoes(paciente.getObservacoes())
                .ativo(paciente.isAtivo())
                .nutricionistaId(paciente.getNutricionista().getId())
                .nutricionistaNome(paciente.getNutricionista().getNome())
                .build();
    }

    private String normalizarOpcional(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
