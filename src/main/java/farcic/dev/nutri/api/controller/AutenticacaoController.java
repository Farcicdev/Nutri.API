package farcic.dev.nutri.api.controller;

import farcic.dev.nutri.api.dto.request.LoginRequest;
import farcic.dev.nutri.api.dto.request.NutricionistaRequest;
import farcic.dev.nutri.api.dto.response.NutricionistaResponse;
import farcic.dev.nutri.api.dto.response.TokenResponse;
import farcic.dev.nutri.api.service.AutenticacaoService;
import farcic.dev.nutri.api.service.NutricionistaService;
import farcic.dev.nutri.api.service.SessaoAutenticada;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AutenticacaoController {

    private static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    private final AutenticacaoService autenticacaoService;
    private final NutricionistaService nutricionistaService;

    @Value("${api.security.refresh-token.secure-cookie:false}")
    private boolean cookieSeguro;

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NutricionistaResponse> cadastrar(@Valid @RequestBody NutricionistaRequest request) {
        NutricionistaResponse response = nutricionistaService.criar(request);
        return ResponseEntity.created(URI.create("/api/nutricionistas/" + response.id())).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return respostaComSessao(autenticacaoService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> renovar(
            @CookieValue(name = REFRESH_TOKEN_COOKIE, required = false) String refreshToken
    ) {
        return respostaComSessao(autenticacaoService.renovar(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = REFRESH_TOKEN_COOKIE, required = false) String refreshToken
    ) {
        autenticacaoService.logout(refreshToken);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookieRemovido().toString())
                .build();
    }

    private ResponseEntity<TokenResponse> respostaComSessao(SessaoAutenticada sessao) {
        ResponseCookie.ResponseCookieBuilder cookie = ResponseCookie
                .from(REFRESH_TOKEN_COOKIE, sessao.refreshToken())
                .httpOnly(true)
                .secure(cookieSeguro)
                .sameSite("Lax")
                .path("/api/auth");

        if (sessao.persistente()) {
            cookie.maxAge(sessao.refreshTokenDuracaoSegundos());
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.build().toString())
                .body(sessao.resposta());
    }

    private ResponseCookie cookieRemovido() {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE, "")
                .httpOnly(true)
                .secure(cookieSeguro)
                .sameSite("Lax")
                .path("/api/auth")
                .maxAge(0)
                .build();
    }
}
