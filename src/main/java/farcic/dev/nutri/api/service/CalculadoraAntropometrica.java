package farcic.dev.nutri.api.service;

import farcic.dev.nutri.api.dto.request.AvaliacaoAntropometricaRequest;
import farcic.dev.nutri.api.entity.enums.FormulaDeConversao;
import farcic.dev.nutri.api.entity.enums.Sexo;
import farcic.dev.nutri.api.exception.RegraDeNegocioException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CalculadoraAntropometrica {

    private static final int ESCALA_RESULTADO = 2;
    private static final int ESCALA_DENSIDADE = 5;

    public ResultadoCalculo calcular(
            AvaliacaoAntropometricaRequest request,
            Sexo sexo,
            int idade
    ) {
        BigDecimal imc = request.peso()
                .divide(request.altura().pow(2), ESCALA_RESULTADO, RoundingMode.HALF_UP);

        ResultadoComposicao composicao = switch (request.protocoloDobras()) {
            case JACKSON_POLLACK_7_PONTOS -> calcularJacksonPollock7(request, sexo, idade);
            case JACKSON_POLLACK_3_PONTOS -> calcularJacksonPollock3(request, sexo, idade);
            case FAULKNER -> calcularFaulkner(request);
        };

        validarPercentualGordura(composicao.percentualGordura());

        BigDecimal massaGorda = request.peso()
                .multiply(composicao.percentualGordura())
                .divide(BigDecimal.valueOf(100), ESCALA_RESULTADO, RoundingMode.HALF_UP);
        BigDecimal massaMagra = request.peso()
                .subtract(massaGorda)
                .setScale(ESCALA_RESULTADO, RoundingMode.HALF_UP);

        return new ResultadoCalculo(
                imc,
                composicao.somaDobras(),
                composicao.densidadeCorporal(),
                composicao.percentualGordura(),
                massaGorda,
                massaMagra,
                composicao.formulaConversao()
        );
    }

    private ResultadoComposicao calcularJacksonPollock7(
            AvaliacaoAntropometricaRequest request,
            Sexo sexo,
            int idade
    ) {
        BigDecimal soma = somar(
                request.peitoral(), request.axilarMedia(), request.tricipital(),
                request.subescapular(), request.abdominal(), request.suprailiaca(), request.coxa()
        );
        double s = soma.doubleValue();
        double densidade = sexo == Sexo.MASCULINO
                ? 1.112 - (0.00043499 * s) + (0.00000055 * s * s) - (0.00028826 * idade)
                : 1.097 - (0.00046971 * s) + (0.00000056 * s * s) - (0.00012828 * idade);
        return resultadoJacksonPollock(soma, densidade);
    }

    private ResultadoComposicao calcularJacksonPollock3(
            AvaliacaoAntropometricaRequest request,
            Sexo sexo,
            int idade
    ) {
        BigDecimal soma = sexo == Sexo.MASCULINO
                ? somar(request.peitoral(), request.abdominal(), request.coxa())
                : somar(request.tricipital(), request.suprailiaca(), request.coxa());
        double s = soma.doubleValue();
        double densidade = sexo == Sexo.MASCULINO
                ? 1.10938 - (0.0008267 * s) + (0.0000016 * s * s) - (0.0002574 * idade)
                : 1.0994921 - (0.0009929 * s) + (0.0000023 * s * s) - (0.0001392 * idade);
        return resultadoJacksonPollock(soma, densidade);
    }

    private ResultadoComposicao resultadoJacksonPollock(BigDecimal soma, double densidade) {
        if (densidade <= 0) {
            throw new RegraDeNegocioException("As medidas informadas produziram uma densidade corporal inválida");
        }
        BigDecimal densidadeArredondada = arredondar(densidade, ESCALA_DENSIDADE);
        BigDecimal percentual = arredondar((495.0 / densidade) - 450.0, ESCALA_RESULTADO);
        return new ResultadoComposicao(soma, densidadeArredondada, percentual, FormulaDeConversao.SIRI);
    }

    private ResultadoComposicao calcularFaulkner(AvaliacaoAntropometricaRequest request) {
        BigDecimal soma = somar(
                request.tricipital(), request.subescapular(),
                request.suprailiaca(), request.abdominal()
        );
        BigDecimal percentual = soma.multiply(BigDecimal.valueOf(0.153))
                .add(BigDecimal.valueOf(5.783))
                .setScale(ESCALA_RESULTADO, RoundingMode.HALF_UP);
        return new ResultadoComposicao(soma, null, percentual, FormulaDeConversao.NAO_APLICAVEL);
    }

    private BigDecimal somar(BigDecimal... valores) {
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal valor : valores) {
            total = total.add(valor);
        }
        return total.setScale(ESCALA_RESULTADO, RoundingMode.HALF_UP);
    }

    private BigDecimal arredondar(double valor, int escala) {
        return BigDecimal.valueOf(valor).setScale(escala, RoundingMode.HALF_UP);
    }

    private void validarPercentualGordura(BigDecimal percentual) {
        if (percentual.signum() < 0 || percentual.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new RegraDeNegocioException(
                    "As medidas informadas produziram um percentual de gordura inválido"
            );
        }
    }

    private record ResultadoComposicao(
            BigDecimal somaDobras,
            BigDecimal densidadeCorporal,
            BigDecimal percentualGordura,
            FormulaDeConversao formulaConversao
    ) {
    }

    public record ResultadoCalculo(
            BigDecimal imc,
            BigDecimal somaDobras,
            BigDecimal densidadeCorporal,
            BigDecimal percentualGordura,
            BigDecimal massaGorda,
            BigDecimal massaMagra,
            FormulaDeConversao formulaConversao
    ) {
    }
}
