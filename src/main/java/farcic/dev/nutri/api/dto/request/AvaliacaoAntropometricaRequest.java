package farcic.dev.nutri.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import farcic.dev.nutri.api.entity.enums.ProtocoloDeDobras;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record AvaliacaoAntropometricaRequest(
        @NotNull @Positive Long consultaId,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 4, fraction = 2)
        BigDecimal peso,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 2, fraction = 2)
        BigDecimal altura,
        @NotNull @PastOrPresent @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime dataAvaliacao,
        @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 4, fraction = 2)
        BigDecimal cintura,
        @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 4, fraction = 2)
        BigDecimal quadril,
        @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 4, fraction = 2)
        BigDecimal peitoral,
        @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 4, fraction = 2)
        BigDecimal tricipital,
        @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 4, fraction = 2)
        BigDecimal subescapular,
        @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 4, fraction = 2)
        BigDecimal bicipital,
        @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 4, fraction = 2)
        BigDecimal suprailiaca,
        @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 4, fraction = 2)
        BigDecimal abdominal,
        @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 4, fraction = 2)
        BigDecimal coxa,
        @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 4, fraction = 2)
        BigDecimal axilarMedia,
        @NotNull ProtocoloDeDobras protocoloDobras,
        @Size(max = 1000) String observacoes
) {
}
