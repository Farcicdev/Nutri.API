package farcic.dev.nutri.api.security;

import farcic.dev.nutri.api.entity.Nutricionista;
import farcic.dev.nutri.api.entity.enums.Role;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioAutenticadoTest {

    @Test
    void deveConverterRoleEmAuthorityDoSpringSecurity() {
        Nutricionista nutricionista = Nutricionista.builder()
                .id(1L)
                .email("ana@exemplo.com")
                .senha("senha-criptografada")
                .role(Role.NUTRICIONISTA)
                .build();

        UsuarioAutenticado usuario = UsuarioAutenticado.from(nutricionista);

        assertThat(usuario.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_NUTRICIONISTA");
        assertThat(usuario.isEnabled()).isTrue();
    }
}
