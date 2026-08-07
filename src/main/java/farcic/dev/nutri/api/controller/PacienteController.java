package farcic.dev.nutri.api.controller;

import farcic.dev.nutri.api.dto.request.PacienteAtivoRequest;
import farcic.dev.nutri.api.dto.request.PacienteRequest;
import farcic.dev.nutri.api.dto.response.PacienteResponse;
import farcic.dev.nutri.api.service.PacienteService;
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
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PacienteResponse> criar(@Valid @RequestBody PacienteRequest request) {
        PacienteResponse response = pacienteService.criar(request);
        return ResponseEntity.created(URI.create("/api/pacientes/" + response.id())).body(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PacienteResponse> listar() {
        return pacienteService.listar();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PacienteResponse buscarPorId(@PathVariable Long id) {
        return pacienteService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PacienteResponse atualizar(@PathVariable Long id, @Valid @RequestBody PacienteRequest request) {
        return pacienteService.atualizar(id, request);
    }

    @PatchMapping("/{id}/ativo")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PacienteResponse atualizarStatus(@PathVariable Long id, @Valid @RequestBody PacienteAtivoRequest request) {
        return pacienteService.atualizarStatus(id, request.ativo());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        pacienteService.remover(id);
    }
}
