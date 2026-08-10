package farcic.dev.nutri.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedidaCorporal {

    private Long id;
    private Double peso;
    private Double altura;
    private Double imc;
    private Double percentualGordura;
    private Double percentualMassaMagra;
    private Double percentualMassaGorda;
    private Double circunferenciaCintura;
    private Double circunferenciaQuadril;
    private Double circunferenciaBracoRelaxado;
    private Double circunferenciaBracoContraido;
    private Double circunferenciaCoxa;
    private Double circunferenciaPanturrilha;

}
