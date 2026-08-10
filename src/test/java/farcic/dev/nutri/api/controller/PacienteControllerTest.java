package farcic.dev.nutri.api.controller;

import farcic.dev.nutri.api.dto.response.PacienteResponse;
import farcic.dev.nutri.api.entity.enums.Sexo;
import farcic.dev.nutri.api.exception.ApiExceptionHandler;
import farcic.dev.nutri.api.exception.RegraDeNegocioException;
import farcic.dev.nutri.api.service.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PacienteControllerTest {

    @Mock
    private PacienteService pacienteService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new PacienteController(pacienteService))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void deveCadastrarPaciente() throws Exception {
        PacienteResponse response = PacienteResponse.builder()
                .id(10L)
                .nome("Maria")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .sexo(Sexo.FEMININO)
                .email("maria@exemplo.com")
                .ativo(true)
                .nutricionistaId(1L)
                .nutricionistaNome("Ana")
                .build();
        when(pacienteService.criar(any())).thenReturn(response);

        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Maria",
                                  "dataNascimento": "01/01/1990",
                                  "sexo": "FEMININO",
                                  "email": "maria@exemplo.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/pacientes/10"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nutricionistaId").value(1));
    }

    @Test
    void deveRetornarErroParaPacienteInvalido() throws Exception {
        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "",
                                  "dataNascimento": "01/01/2999",
                                  "email": "invalido",
                                  "nutricionistaId": 0
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveRetornarConflitoParaRegraDeNegocio() throws Exception {
        when(pacienteService.criar(any()))
                .thenThrow(new RegraDeNegocioException(
                        "Não é possível vincular um paciente a um nutricionista inativo"
                ));

        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Maria",
                                  "dataNascimento": "01/01/1990",
                                  "sexo": "FEMININO",
                                  "email": "maria@exemplo.com",
                                  "nutricionistaId": 1
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Não é possível vincular um paciente a um nutricionista inativo"));
    }
}
