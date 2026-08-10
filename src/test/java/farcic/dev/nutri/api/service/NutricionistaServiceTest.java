package farcic.dev.nutri.api.service;

import farcic.dev.nutri.api.dto.request.NutricionistaRequest;
import farcic.dev.nutri.api.dto.request.NutricionistaAtualizacaoRequest;
import farcic.dev.nutri.api.dto.response.NutricionistaResponse;
import farcic.dev.nutri.api.entity.Nutricionista;
import farcic.dev.nutri.api.exception.RecursoNaoEncontradoException;
import farcic.dev.nutri.api.exception.RegraDeNegocioException;
import farcic.dev.nutri.api.mapper.NutricionistaMapper;
import farcic.dev.nutri.api.repository.NutricionistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NutricionistaServiceTest {

    @Mock
    private NutricionistaRepository nutricionistaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private NutricionistaService nutricionistaService;

    @BeforeEach
    void setUp() {
        nutricionistaService = new NutricionistaService(
                nutricionistaRepository,
                new NutricionistaMapper(),
                passwordEncoder
        );
    }

    @Test
    void deveCadastrarNutricionista() {
        NutricionistaRequest request = requestPadrao();
        when(passwordEncoder.encode("12345678")).thenReturn("senha-criptografada");
        when(nutricionistaRepository.save(any(Nutricionista.class)))
                .thenAnswer(invocation -> {
                    Nutricionista nutricionista = invocation.getArgument(0);
                    nutricionista.setId(1L);
                    return nutricionista;
                });

        NutricionistaResponse response = nutricionistaService.criar(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("ana@exemplo.com");
        assertThat(response.crn()).isEqualTo("CRN-123");
        assertThat(response.ativo()).isTrue();
        verify(passwordEncoder).encode("12345678");
        verify(nutricionistaRepository).save(org.mockito.ArgumentMatchers.argThat(
                nutricionista -> nutricionista.getSenha().equals("senha-criptografada")
        ));
    }

    @Test
    void deveAtualizarNutricionista() {
        Nutricionista nutricionista = nutricionistaPadrao();
        NutricionistaAtualizacaoRequest request = NutricionistaAtualizacaoRequest.builder()
                .nome("Ana Atualizada")
                .email("ANA.NOVA@EXEMPLO.COM")
                .telefone("11911112222")
                .crn("crn-456")
                .especialidade("Esportiva")
                .build();
        when(nutricionistaRepository.findById(1L)).thenReturn(Optional.of(nutricionista));
        when(nutricionistaRepository.save(nutricionista)).thenReturn(nutricionista);

        NutricionistaResponse response = nutricionistaService.atualizar(1L, request);

        assertThat(response.nome()).isEqualTo("Ana Atualizada");
        assertThat(response.email()).isEqualTo("ana.nova@exemplo.com");
        assertThat(response.crn()).isEqualTo("CRN-456");
        assertThat(nutricionista.getSenha()).isEqualTo("12345678");
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void deveAtualizarSenhaCriptografada() {
        Nutricionista nutricionista = nutricionistaPadrao();
        when(nutricionistaRepository.findById(1L)).thenReturn(Optional.of(nutricionista));
        when(passwordEncoder.encode("nova-senha")).thenReturn("novo-hash");

        nutricionistaService.atualizarSenha(1L, "nova-senha");

        assertThat(nutricionista.getSenha()).isEqualTo("novo-hash");
        verify(nutricionistaRepository).save(nutricionista);
    }

    @Test
    void naoDeveCadastrarEmailDuplicado() {
        NutricionistaRequest request = requestPadrao();
        when(nutricionistaRepository.existsByEmailIgnoreCase(request.email())).thenReturn(true);

        assertThatThrownBy(() -> nutricionistaService.criar(request))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessage("Já existe um nutricionista com este e-mail");

        verify(nutricionistaRepository, never()).save(any());
    }

    @Test
    void naoDeveCadastrarCrnDuplicado() {
        NutricionistaRequest request = requestPadrao();
        when(nutricionistaRepository.existsByCrnIgnoreCase(request.crn())).thenReturn(true);

        assertThatThrownBy(() -> nutricionistaService.criar(request))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessage("Já existe um nutricionista com este CRN");

        verify(nutricionistaRepository, never()).save(any());
    }

    @Test
    void deveFalharAoBuscarNutricionistaInexistente() {
        when(nutricionistaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> nutricionistaService.buscarPorId(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Nutricionista com id 99 não encontrado");
    }

    private NutricionistaRequest requestPadrao() {
        return NutricionistaRequest.builder()
                .nome(" Ana ")
                .email("ANA@EXEMPLO.COM")
                .senha("12345678")
                .telefone("11999999999")
                .crn("crn-123")
                .especialidade("Clínica")
                .build();
    }

    private Nutricionista nutricionistaPadrao() {
        return Nutricionista.builder()
                .id(1L)
                .nome("Ana")
                .email("ana@exemplo.com")
                .senha("12345678")
                .telefone("11999999999")
                .crn("CRN-123")
                .especialidade("Clínica")
                .build();
    }
}
