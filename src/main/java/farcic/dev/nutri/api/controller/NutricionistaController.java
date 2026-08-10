package farcic.dev.nutri.api.controller;

import farcic.dev.nutri.api.dto.request.NutricionistaAtualizacaoRequest;
import farcic.dev.nutri.api.dto.request.NutricionistaAtivoRequest;
import farcic.dev.nutri.api.dto.request.NutricionistaRequest;
import farcic.dev.nutri.api.dto.request.NutricionistaSenhaRequest;
import farcic.dev.nutri.api.dto.response.NutricionistaResponse;
import farcic.dev.nutri.api.service.NutricionistaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/nutricionistas")
@RequiredArgsConstructor
public class NutricionistaController {

    private final NutricionistaService nutricionistaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NutricionistaResponse> criar(@Valid @RequestBody NutricionistaRequest request) {
        NutricionistaResponse response = nutricionistaService.criar(request);
        return ResponseEntity.created(URI.create("/api/nutricionistas/" + response.id())).body(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NutricionistaResponse> listar() {
        return nutricionistaService.listar();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NutricionistaResponse buscarPorId(@PathVariable Long id) {
        return nutricionistaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public NutricionistaResponse atualizar(@PathVariable Long id, @Valid @RequestBody NutricionistaAtualizacaoRequest request) {
        return nutricionistaService.atualizar(id, request);
    }

    @PatchMapping("/{id}/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarSenha(
            @PathVariable Long id,
            @Valid @RequestBody NutricionistaSenhaRequest request
    ) {
        nutricionistaService.atualizarSenha(id, request.senha());
    }

    @PatchMapping("/{id}/ativo")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public NutricionistaResponse atualizarStatus(@PathVariable Long id, @Valid @RequestBody NutricionistaAtivoRequest request) {
        return nutricionistaService.atualizarStatus(id, request.ativo());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        nutricionistaService.remover(id);
    }
}
