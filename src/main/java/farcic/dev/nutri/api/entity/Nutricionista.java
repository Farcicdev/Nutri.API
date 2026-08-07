package farcic.dev.nutri.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nutricionistas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nutricionista {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(length = 20)
    private String telefone;

    @Column(nullable = false, unique = true, length = 30)
    private String crn;

    @Column(length = 120)
    private String especialidade;

    @Column(nullable = false)
    @Builder.Default
    private boolean ativo = true;

    @OneToMany
    private Paciente paciente;

}
