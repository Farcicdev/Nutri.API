package farcic.dev.nutri.api.validation;

import farcic.dev.nutri.api.dto.request.NutricionistaAtivoRequest;
import farcic.dev.nutri.api.dto.request.NutricionistaAtualizacaoRequest;
import farcic.dev.nutri.api.dto.request.NutricionistaRequest;
import farcic.dev.nutri.api.dto.request.NutricionistaSenhaRequest;
import farcic.dev.nutri.api.dto.request.PacienteAtivoRequest;
import farcic.dev.nutri.api.dto.request.PacienteRequest;
import farcic.dev.nutri.api.entity.enums.Sexo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void deveAceitarNutricionistaValido() {
        NutricionistaRequest request = NutricionistaRequest.builder()
                .nome("Ana")
                .email("ana@exemplo.com")
                .senha("12345678")
                .crn("CRN-123")
                .build();

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void deveRejeitarNutricionistaInvalido() {
        NutricionistaRequest request = NutricionistaRequest.builder()
                .nome("")
                .email("email-invalido")
                .senha("123")
                .crn("")
                .build();

        Set<ConstraintViolation<NutricionistaRequest>> violations = validator.validate(request);

        assertThat(camposComErro(violations))
                .contains("nome", "email", "senha", "crn");
    }

    @Test
    void deveAceitarPacienteValido() {
        PacienteRequest request = PacienteRequest.builder()
                .nome("Maria")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .sexo(Sexo.FEMININO)
                .email("maria@exemplo.com")
                .nutricionistaId(1L)
                .build();

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void deveRejeitarPacienteInvalido() {
        PacienteRequest request = PacienteRequest.builder()
                .nome("")
                .dataNascimento(LocalDate.now().plusDays(1))
                .email("invalido")
                .nutricionistaId(0L)
                .build();

        Set<ConstraintViolation<PacienteRequest>> violations = validator.validate(request);

        assertThat(camposComErro(violations))
                .contains("nome", "dataNascimento", "sexo", "email", "nutricionistaId");
    }

    @Test
    void deveExigirStatusAtivo() {
        assertThat(validator.validate(NutricionistaAtivoRequest.builder().build())).isNotEmpty();
        assertThat(validator.validate(PacienteAtivoRequest.builder().build())).isNotEmpty();
    }

    @Test
    void deveValidarAtualizacaoSemExigirSenha() {
        NutricionistaAtualizacaoRequest request = NutricionistaAtualizacaoRequest.builder()
                .nome("Ana")
                .email("ana@exemplo.com")
                .crn("CRN-123")
                .build();

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void deveExigirNovaSenhaComOitoCaracteres() {
        NutricionistaSenhaRequest request = NutricionistaSenhaRequest.builder()
                .senha("123")
                .build();

        assertThat(validator.validate(request)).isNotEmpty();
    }

    private <T> Set<String> camposComErro(Set<ConstraintViolation<T>> violations) {
        return violations.stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(java.util.stream.Collectors.toSet());
    }
}
