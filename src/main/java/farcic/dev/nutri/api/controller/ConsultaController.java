package farcic.dev.nutri.api.controller;

import farcic.dev.nutri.api.dto.request.ConsultaRequest;
import farcic.dev.nutri.api.dto.request.ConsultaStatusRequest;
import farcic.dev.nutri.api.dto.response.ConsultaResponse;
import farcic.dev.nutri.api.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ConsultaResponse> criar(@Valid @RequestBody ConsultaRequest request) {
        ConsultaResponse response = consultaService.criar(request);
        return ResponseEntity.created(URI.create("/api/consultas/" + response.id())).body(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ConsultaResponse> listar() {
        return consultaService.listar();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ConsultaResponse buscarPorId(@PathVariable Long id) {
        return consultaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ConsultaResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ConsultaRequest request
    ) {
        return consultaService.atualizar(id, request);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ConsultaResponse atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody ConsultaStatusRequest request
    ) {
        return consultaService.atualizarStatus(id, request.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        consultaService.remover(id);
    }
}
