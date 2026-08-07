package farcic.dev.nutri.api.repository;

import farcic.dev.nutri.api.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByEmailIgnoreCaseAndNutricionistaId(String email, Long nutricionistaId);

    boolean existsByEmailIgnoreCaseAndNutricionistaIdAndIdNot(
            String email,
            Long nutricionistaId,
            Long id
    );
}
