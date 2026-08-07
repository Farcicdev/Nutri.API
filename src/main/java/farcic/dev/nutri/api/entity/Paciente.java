package farcic.dev.nutri.api.entity;

import farcic.dev.nutri.api.entity.enums.Sexo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "pacientes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Sexo sexo;

    @Column(nullable = false, length = 160)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(length = 2000)
    private String observacoes;

    @Column(nullable = false)
    @Builder.Default
    private boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nutricionista_id", nullable = false)
    private Nutricionista nutricionista;

}
