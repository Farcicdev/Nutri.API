package farcic.dev.nutri.api.dto.response;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record ErrorResponse(

        String message,
        LocalDateTime timestamp

) {
}
