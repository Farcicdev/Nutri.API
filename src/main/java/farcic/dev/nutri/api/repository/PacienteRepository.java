package farcic.dev.nutri.api.repository;

import farcic.dev.nutri.api.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByEmailIgnoreCaseAndNutricionistaId(String email, Long nutricionistaId);

    boolean existsByEmailIgnoreCaseAndNutricionistaIdAndIdNot(
            String email,
            Long nutricionistaId,
            Long id
    );

    List<Paciente> findAllByNutricionistaId(Long nutricionistaId);

    Optional<Paciente> findByIdAndNutricionistaId(Long id, Long nutricionistaId);
}
