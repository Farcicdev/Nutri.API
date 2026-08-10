package farcic.dev.nutri.api.entity;

import farcic.dev.nutri.api.entity.enums.FormulaDeConversao;
import farcic.dev.nutri.api.entity.enums.ProtocoloDeDobras;
import farcic.dev.nutri.api.entity.enums.Sexo;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacoes_antropometricas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvaliacaoAntropometrica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dados básicos informados pelo nutricionista
    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal peso;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal altura;

    @Column(name = "data_avaliacao", nullable = false)
    private LocalDateTime dataAvaliacao;

    // Circunferências (cm)
    @Column(precision = 6, scale = 2)
    private BigDecimal cintura;

    @Column(precision = 6, scale = 2)
    private BigDecimal quadril;

    // Dobras cutâneas (mm)
    @Column(precision = 6, scale = 2)
    private BigDecimal peitoral;

    @Column(precision = 6, scale = 2)
    private BigDecimal tricipital;

    @Column(precision = 6, scale = 2)
    private BigDecimal subescapular;

    @Column(precision = 6, scale = 2)
    private BigDecimal bicipital;

    @Column(precision = 6, scale = 2)
    private BigDecimal suprailiaca;

    @Column(precision = 6, scale = 2)
    private BigDecimal abdominal;

    @Column(precision = 6, scale = 2)
    private BigDecimal coxa;

    @Column(name = "axilar_media", precision = 6, scale = 2)
    private BigDecimal axilarMedia;

    // Contexto usado para tornar o cálculo reproduzível
    @Enumerated(EnumType.STRING)
    @Column(name = "protocolo_dobras", nullable = false, length = 40)
    private ProtocoloDeDobras protocoloDobras;

    @Enumerated(EnumType.STRING)
    @Column(name = "formula_conversao", nullable = false, length = 20)
    private FormulaDeConversao formulaConversao;

    @Column(name = "versao_formula", nullable = false)
    @Builder.Default
    private Integer versaoFormula = 1;

    @Column(name = "idade_utilizada_calculo", nullable = false)
    private Integer idadeUtilizadaCalculo;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo_utilizado_calculo", nullable = false, length = 20)
    private Sexo sexoUtilizadoCalculo;

    // Resultados calculados automaticamente
    @Column(name = "soma_dobras", nullable = false, precision = 7, scale = 2)
    private BigDecimal somaDobras;

    @Column(name = "densidade_corporal", precision = 7, scale = 5)
    private BigDecimal densidadeCorporal;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal imc;

    @Column(name = "percentual_gordura", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentualGordura;

    @Column(name = "massa_gorda", nullable = false, precision = 6, scale = 2)
    private BigDecimal massaGorda;

    @Column(name = "massa_magra", nullable = false, precision = 6, scale = 2)
    private BigDecimal massaMagra;

    @Column(length = 1000)
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consulta_id", nullable = false)
    private Consulta consulta;

}
