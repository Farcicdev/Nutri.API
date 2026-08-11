package farcic.dev.nutri.api.service;

import farcic.dev.nutri.api.entity.Nutricionista;
import farcic.dev.nutri.api.entity.RefreshToken;
import farcic.dev.nutri.api.exception.SessaoInvalidaException;
import farcic.dev.nutri.api.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${api.security.refresh-token.session-hours:12}")
    private long sessaoHoras;

    @Value("${api.security.refresh-token.remember-days:30}")
    private long lembrarDias;

    @Transactional
    public TokenCriado criar(Nutricionista nutricionista, boolean persistente) {
        String valor = gerarValorSeguro();
        Duration duracao = persistente
                ? Duration.ofDays(lembrarDias)
                : Duration.ofHours(sessaoHoras);
        Instant agora = Instant.now();

        refreshTokenRepository.save(RefreshToken.builder()
                .tokenHash(hash(valor))
                .nutricionista(nutricionista)
                .persistente(persistente)
                .criadoEm(agora)
                .expiraEm(agora.plus(duracao))
                .build());

        return new TokenCriado(valor, persistente, duracao.toSeconds());
    }

    @Transactional
    public RefreshToken consumir(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new SessaoInvalidaException();
        }

        RefreshToken token = refreshTokenRepository.findByTokenHash(hash(valor))
                .orElseThrow(SessaoInvalidaException::new);
        Instant agora = Instant.now();

        if (token.getRevogadoEm() != null
                || !token.getExpiraEm().isAfter(agora)
                || !token.getNutricionista().isAtivo()) {
            throw new SessaoInvalidaException();
        }

        token.setRevogadoEm(agora);
        return token;
    }

    @Transactional
    public void revogar(String valor) {
        if (valor == null || valor.isBlank()) {
            return;
        }

        refreshTokenRepository.findByTokenHash(hash(valor))
                .filter(token -> token.getRevogadoEm() == null)
                .ifPresent(token -> token.setRevogadoEm(Instant.now()));
    }

    private String gerarValorSeguro() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String valor) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(valor.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 não disponível", exception);
        }
    }

    public record TokenCriado(String valor, boolean persistente, long duracaoSegundos) {
    }
}

