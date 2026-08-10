package farcic.dev.nutri.api.repository;

import farcic.dev.nutri.api.entity.Nutricionista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NutricionistaRepository extends JpaRepository<Nutricionista, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByCrnIgnoreCase(String crn);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByCrnIgnoreCaseAndIdNot(String crn, Long id);

    Optional<Nutricionista> findByEmailIgnoreCase(String email);
}
