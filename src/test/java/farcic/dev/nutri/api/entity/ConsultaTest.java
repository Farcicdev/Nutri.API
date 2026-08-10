package farcic.dev.nutri.api.entity;

import farcic.dev.nutri.api.entity.enums.StatusConsulta;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ConsultaTest {

    @Test
    void deveCriarConsultaComStatusAgendada() {
        Consulta consulta = Consulta.builder().build();

        assertThat(consulta.getStatus()).isEqualTo(StatusConsulta.AGENDADA);
    }

    @Test
    void devePreencherDatasDeAuditoria() {
        Consulta consulta = Consulta.builder().build();

        consulta.prePersist();
        LocalDateTime criadoEm = consulta.getCriadoEm();

        assertThat(criadoEm).isNotNull();
        assertThat(consulta.getAtualizadoEm()).isEqualTo(criadoEm);

        consulta.preUpdate();

        assertThat(consulta.getCriadoEm()).isEqualTo(criadoEm);
        assertThat(consulta.getAtualizadoEm()).isAfterOrEqualTo(criadoEm);
    }
}
