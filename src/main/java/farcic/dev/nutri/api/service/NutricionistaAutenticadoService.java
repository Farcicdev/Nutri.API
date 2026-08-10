package farcic.dev.nutri.api.service;

import farcic.dev.nutri.api.entity.Nutricionista;
import farcic.dev.nutri.api.exception.RecursoNaoEncontradoException;
import farcic.dev.nutri.api.repository.NutricionistaRepository;
import farcic.dev.nutri.api.security.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NutricionistaAutenticadoService {

    private final NutricionistaRepository nutricionistaRepository;

    public Long getId() {
        return getPrincipal().id();
    }

    public Nutricionista getEntidade() {
        Long id = getId();
        return nutricionistaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Nutricionista autenticado não encontrado"
                ));
    }

    private UsuarioAutenticado getPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UsuarioAutenticado usuario) {
            return usuario;
        }
        throw new IllegalStateException("Usuário não autenticado");
    }
}
