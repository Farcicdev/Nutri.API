package farcic.dev.nutri.api.repository;

import farcic.dev.nutri.api.entity.AvaliacaoAntropometrica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AvaliacaoAntropometricaRepository extends JpaRepository<AvaliacaoAntropometrica, Long> {

    List<AvaliacaoAntropometrica> findAllByConsultaIdAndConsultaNutricionistaIdOrderByDataAvaliacaoDesc(
            Long consultaId,
            Long nutricionistaId
    );

    Optional<AvaliacaoAntropometrica> findByIdAndConsultaNutricionistaId(
            Long id,
            Long nutricionistaId
    );
}
