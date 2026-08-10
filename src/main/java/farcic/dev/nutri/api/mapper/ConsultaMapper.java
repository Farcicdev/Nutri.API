package farcic.dev.nutri.api.mapper;

import farcic.dev.nutri.api.dto.request.ConsultaRequest;
import farcic.dev.nutri.api.dto.response.ConsultaResponse;
import farcic.dev.nutri.api.entity.Consulta;
import farcic.dev.nutri.api.entity.Nutricionista;
import farcic.dev.nutri.api.entity.Paciente;
import org.springframework.stereotype.Component;

@Component
public class ConsultaMapper {

    public Consulta toEntity(
            ConsultaRequest request,
            Paciente paciente,
            Nutricionista nutricionista
    ) {
        return Consulta.builder()
                .dataConsulta(request.dataConsulta())
                .tipo(request.tipo())
                .observacoes(normalizarOpcional(request.observacoes()))
                .paciente(paciente)
                .nutricionista(nutricionista)
                .build();
    }

    public void updateEntity(
            Consulta consulta,
            ConsultaRequest request,
            Paciente paciente,
            Nutricionista nutricionista
    ) {
        consulta.setDataConsulta(request.dataConsulta());
        consulta.setTipo(request.tipo());
        consulta.setObservacoes(normalizarOpcional(request.observacoes()));
        consulta.setPaciente(paciente);
        consulta.setNutricionista(nutricionista);
    }

    public ConsultaResponse toResponse(Consulta consulta) {
        return ConsultaResponse.builder()
                .id(consulta.getId())
                .dataConsulta(consulta.getDataConsulta())
                .status(consulta.getStatus())
                .tipo(consulta.getTipo())
                .observacoes(consulta.getObservacoes())
                .criadoEm(consulta.getCriadoEm())
                .atualizadoEm(consulta.getAtualizadoEm())
                .pacienteId(consulta.getPaciente().getId())
                .pacienteNome(consulta.getPaciente().getNome())
                .nutricionistaId(consulta.getNutricionista().getId())
                .nutricionistaNome(consulta.getNutricionista().getNome())
                .build();
    }

    private String normalizarOpcional(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
