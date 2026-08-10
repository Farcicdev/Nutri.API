package farcic.dev.nutri.api.repository;

import farcic.dev.nutri.api.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findAllByNutricionistaIdOrderByDataConsultaDesc(Long nutricionistaId);

    Optional<Consulta> findByIdAndNutricionistaId(Long id, Long nutricionistaId);

    boolean existsByIdAndNutricionistaId(Long id, Long nutricionistaId);
}
