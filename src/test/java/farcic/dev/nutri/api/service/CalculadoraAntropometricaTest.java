package farcic.dev.nutri.api.service;

import farcic.dev.nutri.api.dto.request.AvaliacaoAntropometricaRequest;
import farcic.dev.nutri.api.entity.enums.FormulaDeConversao;
import farcic.dev.nutri.api.entity.enums.ProtocoloDeDobras;
import farcic.dev.nutri.api.entity.enums.Sexo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CalculadoraAntropometricaTest {

    private final CalculadoraAntropometrica calculadora = new CalculadoraAntropometrica();

    @Test
    void deveCalcularJacksonPollock3MasculinoComSiri() {
        AvaliacaoAntropometricaRequest request = AvaliacaoAntropometricaRequest.builder()
                .peso(new BigDecimal("80.00"))
                .altura(new BigDecimal("2.00"))
                .protocoloDobras(ProtocoloDeDobras.JACKSON_POLLACK_3_PONTOS)
                .peitoral(new BigDecimal("10.00"))
                .abdominal(new BigDecimal("20.00"))
                .coxa(new BigDecimal("30.00"))
                .build();

        CalculadoraAntropometrica.ResultadoCalculo resultado = calculadora.calcular(
                request,
                Sexo.MASCULINO,
                30
        );

        assertThat(resultado.imc()).isEqualByComparingTo("20.00");
        assertThat(resultado.somaDobras()).isEqualByComparingTo("60.00");
        assertThat(resultado.densidadeCorporal()).isEqualByComparingTo("1.05782");
        assertThat(resultado.formulaConversao()).isEqualTo(FormulaDeConversao.SIRI);
        assertThat(resultado.massaGorda().add(resultado.massaMagra()))
                .isEqualByComparingTo("80.00");
    }

    @Test
    void deveCalcularFaulknerSemConversaoDeDensidade() {
        AvaliacaoAntropometricaRequest request = AvaliacaoAntropometricaRequest.builder()
                .peso(new BigDecimal("70.00"))
                .altura(new BigDecimal("1.75"))
                .protocoloDobras(ProtocoloDeDobras.FAULKNER)
                .tricipital(BigDecimal.TEN)
                .subescapular(BigDecimal.TEN)
                .suprailiaca(BigDecimal.TEN)
                .abdominal(BigDecimal.TEN)
                .build();

        CalculadoraAntropometrica.ResultadoCalculo resultado = calculadora.calcular(
                request,
                Sexo.FEMININO,
                30
        );

        assertThat(resultado.somaDobras()).isEqualByComparingTo("40.00");
        assertThat(resultado.percentualGordura()).isEqualByComparingTo("11.90");
        assertThat(resultado.densidadeCorporal()).isNull();
        assertThat(resultado.formulaConversao()).isEqualTo(FormulaDeConversao.NAO_APLICAVEL);
    }
}
