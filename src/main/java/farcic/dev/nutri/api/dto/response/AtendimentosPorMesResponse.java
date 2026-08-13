package farcic.dev.nutri.api.dto.response;

public record AtendimentosPorMesResponse(
        int ano,
        int mes,
        long quantidade
){
}
