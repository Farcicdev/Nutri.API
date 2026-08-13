package farcic.dev.nutri.api.repository;

import farcic.dev.nutri.api.entity.AvaliacaoAntropometrica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface AvaliacaoAntropometricaRepository extends JpaRepository<AvaliacaoAntropometrica, Long> {

    long countByConsultaNutricionistaId(Long nutricionistaId);

    long countByConsultaNutricionistaIdAndDataAvaliacaoGreaterThanEqualAndDataAvaliacaoLessThan(
            Long nutricionistaId,
            LocalDateTime inicio,
            LocalDateTime fimExclusivo
    );

    Optional<AvaliacaoAntropometrica> findFirstByConsultaPacienteIdAndConsultaNutricionistaIdOrderByDataAvaliacaoDesc(
            Long pacienteId,
            Long nutricionistaId
    );

    List<AvaliacaoAntropometrica> findAllByConsultaIdAndConsultaNutricionistaIdOrderByDataAvaliacaoDesc(
            Long consultaId,
            Long nutricionistaId
    );

    Optional<AvaliacaoAntropometrica> findByIdAndConsultaNutricionistaId(
            Long id,
            Long nutricionistaId
    );
}
