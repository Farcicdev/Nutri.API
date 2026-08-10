package farcic.dev.nutri.api.controller;

import farcic.dev.nutri.api.dto.response.NutricionistaResponse;
import farcic.dev.nutri.api.exception.ApiExceptionHandler;
import farcic.dev.nutri.api.exception.RecursoNaoEncontradoException;
import farcic.dev.nutri.api.service.NutricionistaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class NutricionistaControllerTest {

    @Mock
    private NutricionistaService nutricionistaService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new NutricionistaController(nutricionistaService))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void deveCadastrarNutricionista() throws Exception {
        NutricionistaResponse response = NutricionistaResponse.builder()
                .id(1L)
                .nome("Ana")
                .email("ana@exemplo.com")
                .crn("CRN-123")
                .ativo(true)
                .build();
        when(nutricionistaService.criar(any())).thenReturn(response);

        mockMvc.perform(post("/api/nutricionistas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Ana",
                                  "email": "ana@exemplo.com",
                                  "senha": "12345678",
                                  "crn": "CRN-123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/nutricionistas/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("ana@exemplo.com"))
                .andExpect(jsonPath("$.senha").doesNotExist());
    }

    @Test
    void deveRetornarErroParaCadastroInvalido() throws Exception {
        mockMvc.perform(post("/api/nutricionistas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "",
                                  "email": "invalido",
                                  "senha": "123",
                                  "crn": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveRetornarErroAoBuscarNutricionistaInexistente() throws Exception {
        when(nutricionistaService.buscarPorId(99L))
                .thenThrow(new RecursoNaoEncontradoException("Nutricionista com id 99 não encontrado"));

        mockMvc.perform(get("/api/nutricionistas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Nutricionista com id 99 não encontrado"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveAtualizarSenha() throws Exception {
        mockMvc.perform(patch("/api/nutricionistas/1/senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "senha": "nova-senha"
                                }
                                """))
                .andExpect(status().isNoContent());

        verify(nutricionistaService).atualizarSenha(1L, "nova-senha");
    }
}
