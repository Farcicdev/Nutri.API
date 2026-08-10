package farcic.dev.nutri.api.service;

import farcic.dev.nutri.api.dto.request.AvaliacaoAntropometricaRequest;
import farcic.dev.nutri.api.entity.enums.ProtocoloDeDobras;
import farcic.dev.nutri.api.entity.enums.Sexo;
import farcic.dev.nutri.api.exception.RegraDeNegocioException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidadorProtocoloDobrasTest {

    private final ValidadorProtocoloDobras validador = new ValidadorProtocoloDobras();

    @Test
    void deveAceitarAsSeteDobrasDoJacksonPollock7() {
        AvaliacaoAntropometricaRequest request = requestCompleto(ProtocoloDeDobras.JACKSON_POLLACK_7_PONTOS);

        assertThatCode(() -> validador.validar(request, Sexo.MASCULINO))
                .doesNotThrowAnyException();
    }

    @Test
    void deveExigirPeitoralAbdominalECoxaParaHomemNoJacksonPollock3() {
        AvaliacaoAntropometricaRequest request = AvaliacaoAntropometricaRequest.builder()
                .protocoloDobras(ProtocoloDeDobras.JACKSON_POLLACK_3_PONTOS)
                .coxa(BigDecimal.TEN)
                .build();

        assertThatThrownBy(() -> validador.validar(request, Sexo.MASCULINO))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("peitoral")
                .hasMessageContaining("abdominal");
    }

    @Test
    void deveExigirTricipitalSuprailiacaECoxaParaMulherNoJacksonPollock3() {
        AvaliacaoAntropometricaRequest request = AvaliacaoAntropometricaRequest.builder()
                .protocoloDobras(ProtocoloDeDobras.JACKSON_POLLACK_3_PONTOS)
                .coxa(BigDecimal.TEN)
                .build();

        assertThatThrownBy(() -> validador.validar(request, Sexo.FEMININO))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("tricipital")
                .hasMessageContaining("suprailiaca");
    }

    @Test
    void deveExigirAsQuatroDobrasDoFaulkner() {
        AvaliacaoAntropometricaRequest request = AvaliacaoAntropometricaRequest.builder()
                .protocoloDobras(ProtocoloDeDobras.FAULKNER)
                .tricipital(BigDecimal.TEN)
                .build();

        assertThatThrownBy(() -> validador.validar(request, Sexo.FEMININO))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("subescapular")
                .hasMessageContaining("suprailiaca")
                .hasMessageContaining("abdominal");
    }

    private AvaliacaoAntropometricaRequest requestCompleto(ProtocoloDeDobras protocolo) {
        return AvaliacaoAntropometricaRequest.builder()
                .protocoloDobras(protocolo)
                .peitoral(BigDecimal.TEN)
                .axilarMedia(BigDecimal.TEN)
                .tricipital(BigDecimal.TEN)
                .subescapular(BigDecimal.TEN)
                .abdominal(BigDecimal.TEN)
                .suprailiaca(BigDecimal.TEN)
                .coxa(BigDecimal.TEN)
                .build();
    }
}
