package farcic.dev.nutri.api.mapper;

import farcic.dev.nutri.api.dto.request.NutricionistaRequest;
import farcic.dev.nutri.api.dto.response.NutricionistaResponse;
import farcic.dev.nutri.api.entity.Nutricionista;
import org.springframework.stereotype.Component;

@Component
public class NutricionistaMapper {

    public Nutricionista toEntity(NutricionistaRequest request) {
        return Nutricionista.builder()
                .nome(request.nome().trim())
                .email(request.email().trim().toLowerCase())
                .senha(request.senha())
                .telefone(normalizarOpcional(request.telefone()))
                .crn(request.crn().trim().toUpperCase())
                .especialidade(normalizarOpcional(request.especialidade()))
                .build();
    }

    public void updateEntity(Nutricionista nutricionista, NutricionistaRequest request) {
        nutricionista.setNome(request.nome().trim());
        nutricionista.setEmail(request.email().trim().toLowerCase());
        nutricionista.setSenha(request.senha());
        nutricionista.setTelefone(normalizarOpcional(request.telefone()));
        nutricionista.setCrn(request.crn().trim().toUpperCase());
        nutricionista.setEspecialidade(normalizarOpcional(request.especialidade()));
    }

    public NutricionistaResponse toResponse(Nutricionista nutricionista) {
        return NutricionistaResponse.builder()
                .id(nutricionista.getId())
                .nome(nutricionista.getNome())
                .email(nutricionista.getEmail())
                .telefone(nutricionista.getTelefone())
                .crn(nutricionista.getCrn())
                .especialidade(nutricionista.getEspecialidade())
                .ativo(nutricionista.isAtivo())
                .build();
    }

    private String normalizarOpcional(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
