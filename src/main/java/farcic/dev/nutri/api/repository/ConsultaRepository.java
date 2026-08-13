package farcic.dev.nutri.api.repository;

import farcic.dev.nutri.api.dto.response.AtendimentosPorMesResponse;
import farcic.dev.nutri.api.entity.Consulta;
import farcic.dev.nutri.api.entity.enums.StatusConsulta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    long countByNutricionistaIdAndStatusNotAndDataConsultaGreaterThanEqualAndDataConsultaLessThan(
            Long nutricionistaId,
            StatusConsulta status,
            LocalDateTime inicio,
            LocalDateTime fimExclusivo
    );

    long countByNutricionistaIdAndStatusAndDataConsultaGreaterThanEqualAndDataConsultaLessThan(
            Long nutricionistaId,
            StatusConsulta status,
            LocalDateTime inicio,
            LocalDateTime fimExclusivo
    );

    @Query("""
            select new farcic.dev.nutri.api.dto.response.AtendimentosPorMesResponse(
                year(c.dataConsulta), month(c.dataConsulta), count(c)
            )
            from Consulta c
            where c.nutricionista.id = :nutricionistaId
              and c.status = :status
              and c.dataConsulta >= :inicio
              and c.dataConsulta < :fimExclusivo
            group by year(c.dataConsulta), month(c.dataConsulta)
            order by year(c.dataConsulta), month(c.dataConsulta)
            """)
    List<AtendimentosPorMesResponse> contarAtendimentosPorMes(
            @Param("nutricionistaId") Long nutricionistaId,
            @Param("status") StatusConsulta status,
            @Param("inicio") LocalDateTime inicio,
            @Param("fimExclusivo") LocalDateTime fimExclusivo
    );

    @Query("""
            select c
            from Consulta c
            join fetch c.paciente
            where c.nutricionista.id = :nutricionistaId
              and c.status <> :statusCancelada
              and c.dataConsulta >= :agora
            order by c.dataConsulta
            """)
    List<Consulta> buscarProximasConsultas(
            @Param("nutricionistaId") Long nutricionistaId,
            @Param("statusCancelada") StatusConsulta statusCancelada,
            @Param("agora") LocalDateTime agora,
            Pageable pageable
    );

    @Query("""
            select c
            from Consulta c
            join fetch c.paciente p
            where c.nutricionista.id = :nutricionistaId
              and c.dataConsulta = (
                  select max(c2.dataConsulta)
                  from Consulta c2
                  where c2.nutricionista.id = :nutricionistaId
                    and c2.paciente.id = p.id
                    and c2.dataConsulta <= :agora
              )
            order by c.dataConsulta desc
            """)
    List<Consulta> buscarUltimaConsultaDosPacientes(
            @Param("nutricionistaId") Long nutricionistaId,
            @Param("agora") LocalDateTime agora,
            Pageable pageable
    );

    List<Consulta> findAllByNutricionistaIdOrderByDataConsultaDesc(Long nutricionistaId);

    Optional<Consulta> findByIdAndNutricionistaId(Long id, Long nutricionistaId);

    boolean existsByIdAndNutricionistaId(Long id, Long nutricionistaId);
}
