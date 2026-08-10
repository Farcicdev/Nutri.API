package farcic.dev.nutri.api.controller;

import farcic.dev.nutri.api.dto.request.LoginRequest;
import farcic.dev.nutri.api.dto.request.NutricionistaRequest;
import farcic.dev.nutri.api.dto.response.NutricionistaResponse;
import farcic.dev.nutri.api.dto.response.TokenResponse;
import farcic.dev.nutri.api.service.AutenticacaoService;
import farcic.dev.nutri.api.service.NutricionistaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final AutenticacaoService autenticacaoService;
    private final NutricionistaService nutricionistaService;

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NutricionistaResponse> cadastrar(@Valid @RequestBody NutricionistaRequest request) {
        NutricionistaResponse response = nutricionistaService.criar(request);
        return ResponseEntity.created(URI.create("/api/nutricionistas/" + response.id())).body(response);
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return autenticacaoService.login(request);
    }
}
