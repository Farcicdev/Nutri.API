package farcic.dev.nutri.api.dto.response;

public record UsuarioAtualResponse(
        Long id,
        String nome,
        String email,
        String role

) {
}
