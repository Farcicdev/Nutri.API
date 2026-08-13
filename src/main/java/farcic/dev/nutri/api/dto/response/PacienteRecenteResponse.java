package farcic.dev.nutri.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PacienteRecenteResponse(
        Long id,
        String nome,
        LocalDateTime ultimaConsulta,
        BigDecimal imcAtual,
        BigDecimal percentualGorduraAtual

) {
}
