package farcic.dev.nutri.api.service;

import farcic.dev.nutri.api.dto.request.AvaliacaoAntropometricaRequest;
import farcic.dev.nutri.api.entity.enums.Sexo;
import farcic.dev.nutri.api.exception.RegraDeNegocioException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ValidadorProtocoloDobras {

    public void validar(AvaliacaoAntropometricaRequest request, Sexo sexo) {
        List<String> ausentes = switch (request.protocoloDobras()) {
            case JACKSON_POLLACK_7_PONTOS -> validarJacksonPollock7(request);
            case JACKSON_POLLACK_3_PONTOS -> validarJacksonPollock3(request, sexo);
            case FAULKNER -> validarFaulkner(request);
        };

        if (!ausentes.isEmpty()) {
            throw new RegraDeNegocioException(
                    "Dobras obrigatórias ausentes para " + request.protocoloDobras() + ": "
                            + String.join(", ", ausentes)
            );
        }
    }

    private List<String> validarJacksonPollock7(AvaliacaoAntropometricaRequest request) {
        List<String> ausentes = new ArrayList<>();
        exigir(ausentes, "peitoral", request.peitoral());
        exigir(ausentes, "axilarMedia", request.axilarMedia());
        exigir(ausentes, "tricipital", request.tricipital());
        exigir(ausentes, "subescapular", request.subescapular());
        exigir(ausentes, "abdominal", request.abdominal());
        exigir(ausentes, "suprailiaca", request.suprailiaca());
        exigir(ausentes, "coxa", request.coxa());
        return ausentes;
    }

    private List<String> validarJacksonPollock3(AvaliacaoAntropometricaRequest request, Sexo sexo) {
        List<String> ausentes = new ArrayList<>();
        if (sexo == Sexo.MASCULINO) {
            exigir(ausentes, "peitoral", request.peitoral());
            exigir(ausentes, "abdominal", request.abdominal());
        } else {
            exigir(ausentes, "tricipital", request.tricipital());
            exigir(ausentes, "suprailiaca", request.suprailiaca());
        }
        exigir(ausentes, "coxa", request.coxa());
        return ausentes;
    }

    private List<String> validarFaulkner(AvaliacaoAntropometricaRequest request) {
        List<String> ausentes = new ArrayList<>();
        exigir(ausentes, "tricipital", request.tricipital());
        exigir(ausentes, "subescapular", request.subescapular());
        exigir(ausentes, "suprailiaca", request.suprailiaca());
        exigir(ausentes, "abdominal", request.abdominal());
        return ausentes;
    }

    private void exigir(List<String> ausentes, String nome, BigDecimal valor) {
        if (valor == null) {
            ausentes.add(nome);
        }
    }
}
