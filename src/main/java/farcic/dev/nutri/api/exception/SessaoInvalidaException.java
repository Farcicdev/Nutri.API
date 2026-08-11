package farcic.dev.nutri.api.exception;

public class SessaoInvalidaException extends RuntimeException {

    public SessaoInvalidaException() {
        super("Sessão inválida ou expirada");
    }
}

