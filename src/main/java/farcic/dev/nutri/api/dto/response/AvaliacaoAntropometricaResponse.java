package farcic.dev.nutri.api.dto.response;

import farcic.dev.nutri.api.entity.enums.FormulaDeConversao;
import farcic.dev.nutri.api.entity.enums.ProtocoloDeDobras;
import farcic.dev.nutri.api.entity.enums.Sexo;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record AvaliacaoAntropometricaResponse(
        Long id,
        Long consultaId,
        Long pacienteId,
        LocalDateTime dataAvaliacao,
        BigDecimal peso,
        BigDecimal altura,
        BigDecimal cintura,
        BigDecimal quadril,
        BigDecimal peitoral,
        BigDecimal tricipital,
        BigDecimal subescapular,
        BigDecimal bicipital,
        BigDecimal suprailiaca,
        BigDecimal abdominal,
        BigDecimal coxa,
        BigDecimal axilarMedia,
        ProtocoloDeDobras protocoloDobras,
        FormulaDeConversao formulaConversao,
        Integer versaoFormula,
        Integer idadeUtilizadaCalculo,
        Sexo sexoUtilizadoCalculo,
        BigDecimal somaDobras,
        BigDecimal densidadeCorporal,
        BigDecimal imc,
        BigDecimal percentualGordura,
        BigDecimal massaGorda,
        BigDecimal massaMagra,
        String observacoes
) {
}
