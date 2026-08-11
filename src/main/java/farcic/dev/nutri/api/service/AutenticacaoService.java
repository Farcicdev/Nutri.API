package farcic.dev.nutri.api.service;

import farcic.dev.nutri.api.dto.request.LoginRequest;
import farcic.dev.nutri.api.dto.response.TokenResponse;
import farcic.dev.nutri.api.entity.Nutricionista;
import farcic.dev.nutri.api.entity.RefreshToken;
import farcic.dev.nutri.api.exception.SessaoInvalidaException;
import farcic.dev.nutri.api.repository.NutricionistaRepository;
import farcic.dev.nutri.api.security.JwtService;
import farcic.dev.nutri.api.security.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final NutricionistaRepository nutricionistaRepository;

    public SessaoAutenticada login(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                        request.email().trim().toLowerCase(),
                        request.senha()
                )
        );
        UsuarioAutenticado usuario = (UsuarioAutenticado) authentication.getPrincipal();
        Nutricionista nutricionista = nutricionistaRepository.findById(usuario.id())
                .orElseThrow(SessaoInvalidaException::new);
        return criarSessao(usuario, nutricionista, request.lembrarDeMim());
    }

    public SessaoAutenticada renovar(String refreshToken) {
        RefreshToken tokenAnterior = refreshTokenService.consumir(refreshToken);
        Nutricionista nutricionista = tokenAnterior.getNutricionista();
        return criarSessao(
                UsuarioAutenticado.from(nutricionista),
                nutricionista,
                tokenAnterior.isPersistente()
        );
    }

    public void logout(String refreshToken) {
        refreshTokenService.revogar(refreshToken);
    }

    private SessaoAutenticada criarSessao(
            UsuarioAutenticado usuario,
            Nutricionista nutricionista,
            boolean persistente
    ) {
        RefreshTokenService.TokenCriado refreshToken =
                refreshTokenService.criar(nutricionista, persistente);
        TokenResponse resposta = new TokenResponse(
                jwtService.gerarToken(usuario),
                "Bearer",
                jwtService.expiracaoEmSegundos()
        );
        return new SessaoAutenticada(
                resposta,
                refreshToken.valor(),
                refreshToken.persistente(),
                refreshToken.duracaoSegundos()
        );
    }
}
