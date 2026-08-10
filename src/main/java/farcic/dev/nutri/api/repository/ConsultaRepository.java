package farcic.dev.nutri.api.repository;

import farcic.dev.nutri.api.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findAllByOrderByDataConsultaDesc();
}
