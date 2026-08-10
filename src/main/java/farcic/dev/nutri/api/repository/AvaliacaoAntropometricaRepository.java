package farcic.dev.nutri.api.repository;

import farcic.dev.nutri.api.entity.AvaliacaoAntropometrica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvaliacaoAntropometricaRepository extends JpaRepository<AvaliacaoAntropometrica, Long> {

    List<AvaliacaoAntropometrica> findAllByConsultaIdOrderByDataAvaliacaoDesc(Long consultaId);
}
