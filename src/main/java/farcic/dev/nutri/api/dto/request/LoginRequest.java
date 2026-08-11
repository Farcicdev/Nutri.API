package farcic.dev.nutri.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String senha,
        boolean lembrarDeMim
) {
    public LoginRequest(String email, String senha) {
        this(email, senha, false);
    }
}
