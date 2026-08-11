package farcic.dev.nutri.api.service;

import farcic.dev.nutri.api.dto.response.TokenResponse;

public record SessaoAutenticada(
        TokenResponse resposta,
        String refreshToken,
        boolean persistente,
        long refreshTokenDuracaoSegundos
) {
}

