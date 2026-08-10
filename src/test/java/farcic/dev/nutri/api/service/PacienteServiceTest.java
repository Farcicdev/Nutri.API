package farcic.dev.nutri.api.service;

import farcic.dev.nutri.api.dto.request.PacienteRequest;
import farcic.dev.nutri.api.dto.response.PacienteResponse;
import farcic.dev.nutri.api.entity.Nutricionista;
import farcic.dev.nutri.api.entity.Paciente;
import farcic.dev.nutri.api.entity.enums.Sexo;
import farcic.dev.nutri.api.exception.RecursoNaoEncontradoException;
import farcic.dev.nutri.api.exception.RegraDeNegocioException;
import farcic.dev.nutri.api.mapper.PacienteMapper;
import farcic.dev.nutri.api.repository.NutricionistaRepository;
import farcic.dev.nutri.api.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private NutricionistaRepository nutricionistaRepository;

    private PacienteService pacienteService;

    @BeforeEach
    void setUp() {
        pacienteService = new PacienteService(
                pacienteRepository,
                nutricionistaRepository,
                new PacienteMapper()
        );
    }

    @Test
    void deveCadastrarPaciente() {
        Nutricionista nutricionista = nutricionistaAtivo();
        when(nutricionistaRepository.findById(1L)).thenReturn(Optional.of(nutricionista));
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(invocation -> {
            Paciente paciente = invocation.getArgument(0);
            paciente.setId(10L);
            return paciente;
        });

        PacienteResponse response = pacienteService.criar(requestPadrao());

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.email()).isEqualTo("maria@exemplo.com");
        assertThat(response.nutricionistaId()).isEqualTo(1L);
        assertThat(response.nutricionistaNome()).isEqualTo("Ana");
        assertThat(response.ativo()).isTrue();
    }

    @Test
    void naoDeveCadastrarComNutricionistaInexistente() {
        when(nutricionistaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pacienteService.criar(requestPadrao()))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Nutricionista com id 1 não encontrado");

        verify(pacienteRepository, never()).save(any());
    }

    @Test
    void naoDeveCadastrarComNutricionistaInativo() {
        Nutricionista nutricionista = nutricionistaAtivo();
        nutricionista.setAtivo(false);
        when(nutricionistaRepository.findById(1L)).thenReturn(Optional.of(nutricionista));

        assertThatThrownBy(() -> pacienteService.criar(requestPadrao()))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessage("Não é possível vincular um paciente a um nutricionista inativo");

        verify(pacienteRepository, never()).save(any());
    }

    @Test
    void naoDeveCadastrarEmailDuplicadoParaMesmoNutricionista() {
        when(nutricionistaRepository.findById(1L)).thenReturn(Optional.of(nutricionistaAtivo()));
        when(pacienteRepository.existsByEmailIgnoreCaseAndNutricionistaId(
                "MARIA@EXEMPLO.COM",
                1L
        )).thenReturn(true);

        assertThatThrownBy(() -> pacienteService.criar(requestPadrao()))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessage("Já existe um paciente com este e-mail para o nutricionista");

        verify(pacienteRepository, never()).save(any());
    }

    @Test
    void deveAtualizarPaciente() {
        Nutricionista nutricionista = nutricionistaAtivo();
        Paciente paciente = Paciente.builder()
                .id(10L)
                .nome("Maria")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .sexo(Sexo.FEMININO)
                .email("maria@exemplo.com")
                .nutricionista(nutricionista)
                .build();
        when(pacienteRepository.findById(10L)).thenReturn(Optional.of(paciente));
        when(nutricionistaRepository.findById(1L)).thenReturn(Optional.of(nutricionista));
        when(pacienteRepository.save(paciente)).thenReturn(paciente);

        PacienteResponse response = pacienteService.atualizar(10L, requestPadrao());

        assertThat(response.nome()).isEqualTo("Maria");
        assertThat(response.email()).isEqualTo("maria@exemplo.com");
        verify(pacienteRepository).existsByEmailIgnoreCaseAndNutricionistaIdAndIdNot(
                "MARIA@EXEMPLO.COM",
                1L,
                10L
        );
    }

    private PacienteRequest requestPadrao() {
        return PacienteRequest.builder()
                .nome(" Maria ")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .sexo(Sexo.FEMININO)
                .email("MARIA@EXEMPLO.COM")
                .telefone("11999999999")
                .observacoes("Acompanhamento")
                .nutricionistaId(1L)
                .build();
    }

    private Nutricionista nutricionistaAtivo() {
        return Nutricionista.builder()
                .id(1L)
                .nome("Ana")
                .email("ana@exemplo.com")
                .senha("12345678")
                .crn("CRN-123")
                .build();
    }
}
