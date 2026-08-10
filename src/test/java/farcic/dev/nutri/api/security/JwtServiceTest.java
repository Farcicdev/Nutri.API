package farcic.dev.nutri.api.security;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private static final String SEGREDO_BASE64 =
            "bXVpdG8tc2VndXJvLXNlZ3JlZG8tZGVzZW52b2x2aW1lbnRv";

    private final JwtService jwtService = new JwtService(SEGREDO_BASE64, 60);

    @Test
    void deveGerarTokenComIdDoNutricionista() {
        UsuarioAutenticado usuario = new UsuarioAutenticado(
                10L,
                "ana@exemplo.com",
                "senha-criptografada",
                true,
                "NUTRICIONISTA"
        );

        String token = jwtService.gerarToken(usuario);

        assertThat(jwtService.extrairNutricionistaId(token)).isEqualTo(10L);
        assertThat(jwtService.expiracaoEmSegundos()).isEqualTo(3600);
    }

    @Test
    void deveRejeitarTokenAdulterado() {
        UsuarioAutenticado usuario = new UsuarioAutenticado(
                10L,
                "ana@exemplo.com",
                "senha-criptografada",
                true,
                "NUTRICIONISTA"
        );
        String token = jwtService.gerarToken(usuario);
        String adulterado = token.substring(0, token.length() - 1)
                + (token.endsWith("a") ? "b" : "a");

        assertThatThrownBy(() -> jwtService.extrairNutricionistaId(adulterado))
                .isInstanceOf(JwtException.class);
    }
}
