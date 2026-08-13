package farcic.dev.nutri.api.controller;

import farcic.dev.nutri.api.dto.request.AvaliacaoAntropometricaRequest;
import farcic.dev.nutri.api.dto.response.AvaliacaoAntropometricaResponse;
import farcic.dev.nutri.api.service.AvaliacaoAntropometricaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes-antropometricas")
@RequiredArgsConstructor
public class AvaliacaoAntropometricaController {

    private final AvaliacaoAntropometricaService avaliacaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AvaliacaoAntropometricaResponse> criar(@Valid @RequestBody AvaliacaoAntropometricaRequest request) {
        AvaliacaoAntropometricaResponse response = avaliacaoService.criar(request);
        URI location = URI.create("/api/avaliacoes-antropometricas/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AvaliacaoAntropometricaResponse buscarPorId(@PathVariable Long id) {
        return avaliacaoService.buscarPorId(id);
    }

    @GetMapping("/consulta/{consultaId}")
    @ResponseStatus(HttpStatus.OK)
    public List<AvaliacaoAntropometricaResponse> listarPorConsulta(@PathVariable Long consultaId) {
        return avaliacaoService.listarPorConsulta(consultaId);
    }
}
