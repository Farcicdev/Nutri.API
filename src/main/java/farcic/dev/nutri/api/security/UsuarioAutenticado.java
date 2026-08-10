package farcic.dev.nutri.api.security;

import farcic.dev.nutri.api.entity.Nutricionista;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record UsuarioAutenticado(
        Long id,
        String email,
        String senha,
        boolean ativo,
        String role
) implements UserDetails {

    public static UsuarioAutenticado from(Nutricionista nutricionista) {
        return new UsuarioAutenticado(
                nutricionista.getId(),
                nutricionista.getEmail(),
                nutricionista.getSenha(),
                nutricionista.isAtivo(),
                nutricionista.getRole().name()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }
}
