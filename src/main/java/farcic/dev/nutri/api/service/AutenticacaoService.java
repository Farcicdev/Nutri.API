package farcic.dev.nutri.api.service;

import farcic.dev.nutri.api.dto.request.LoginRequest;
import farcic.dev.nutri.api.dto.response.TokenResponse;
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

    public TokenResponse login(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                        request.email().trim().toLowerCase(),
                        request.senha()
                )
        );
        UsuarioAutenticado usuario = (UsuarioAutenticado) authentication.getPrincipal();
        return new TokenResponse(
                jwtService.gerarToken(usuario),
                "Bearer",
                jwtService.expiracaoEmSegundos()
        );
    }
}
