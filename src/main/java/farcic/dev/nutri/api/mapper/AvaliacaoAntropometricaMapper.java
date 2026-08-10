package farcic.dev.nutri.api.mapper;

import farcic.dev.nutri.api.dto.request.AvaliacaoAntropometricaRequest;
import farcic.dev.nutri.api.dto.response.AvaliacaoAntropometricaResponse;
import farcic.dev.nutri.api.entity.AvaliacaoAntropometrica;
import farcic.dev.nutri.api.entity.Consulta;
import org.springframework.stereotype.Component;

@Component
public class AvaliacaoAntropometricaMapper {

    public AvaliacaoAntropometrica toEntity(AvaliacaoAntropometricaRequest request, Consulta consulta) {
        return AvaliacaoAntropometrica.builder()
                .consulta(consulta)
                .dataAvaliacao(request.dataAvaliacao())
                .peso(request.peso())
                .altura(request.altura())
                .cintura(request.cintura())
                .quadril(request.quadril())
                .peitoral(request.peitoral())
                .tricipital(request.tricipital())
                .subescapular(request.subescapular())
                .bicipital(request.bicipital())
                .suprailiaca(request.suprailiaca())
                .abdominal(request.abdominal())
                .coxa(request.coxa())
                .axilarMedia(request.axilarMedia())
                .protocoloDobras(request.protocoloDobras())
                .observacoes(normalizarOpcional(request.observacoes()))
                .build();
    }

    public AvaliacaoAntropometricaResponse toResponse(AvaliacaoAntropometrica avaliacao) {
        return AvaliacaoAntropometricaResponse.builder()
                .id(avaliacao.getId())
                .consultaId(avaliacao.getConsulta().getId())
                .pacienteId(avaliacao.getConsulta().getPaciente().getId())
                .dataAvaliacao(avaliacao.getDataAvaliacao())
                .peso(avaliacao.getPeso())
                .altura(avaliacao.getAltura())
                .cintura(avaliacao.getCintura())
                .quadril(avaliacao.getQuadril())
                .peitoral(avaliacao.getPeitoral())
                .tricipital(avaliacao.getTricipital())
                .subescapular(avaliacao.getSubescapular())
                .bicipital(avaliacao.getBicipital())
                .suprailiaca(avaliacao.getSuprailiaca())
                .abdominal(avaliacao.getAbdominal())
                .coxa(avaliacao.getCoxa())
                .axilarMedia(avaliacao.getAxilarMedia())
                .protocoloDobras(avaliacao.getProtocoloDobras())
                .formulaConversao(avaliacao.getFormulaConversao())
                .versaoFormula(avaliacao.getVersaoFormula())
                .idadeUtilizadaCalculo(avaliacao.getIdadeUtilizadaCalculo())
                .sexoUtilizadoCalculo(avaliacao.getSexoUtilizadoCalculo())
                .somaDobras(avaliacao.getSomaDobras())
                .densidadeCorporal(avaliacao.getDensidadeCorporal())
                .imc(avaliacao.getImc())
                .percentualGordura(avaliacao.getPercentualGordura())
                .massaGorda(avaliacao.getMassaGorda())
                .massaMagra(avaliacao.getMassaMagra())
                .observacoes(avaliacao.getObservacoes())
                .build();
    }

    private String normalizarOpcional(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
