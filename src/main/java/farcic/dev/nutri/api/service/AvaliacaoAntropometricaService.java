package farcic.dev.nutri.api.service;

import farcic.dev.nutri.api.dto.request.AvaliacaoAntropometricaRequest;
import farcic.dev.nutri.api.dto.response.AvaliacaoAntropometricaResponse;
import farcic.dev.nutri.api.entity.AvaliacaoAntropometrica;
import farcic.dev.nutri.api.entity.Consulta;
import farcic.dev.nutri.api.entity.Paciente;
import farcic.dev.nutri.api.exception.RecursoNaoEncontradoException;
import farcic.dev.nutri.api.exception.RegraDeNegocioException;
import farcic.dev.nutri.api.mapper.AvaliacaoAntropometricaMapper;
import farcic.dev.nutri.api.repository.AvaliacaoAntropometricaRepository;
import farcic.dev.nutri.api.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliacaoAntropometricaService {

    private static final int VERSAO_FORMULA = 1;

    private final AvaliacaoAntropometricaRepository avaliacaoRepository;
    private final ConsultaRepository consultaRepository;
    private final AvaliacaoAntropometricaMapper avaliacaoMapper;
    private final ValidadorProtocoloDobras validadorProtocolo;
    private final CalculadoraAntropometrica calculadora;

    @Transactional
    public AvaliacaoAntropometricaResponse criar(AvaliacaoAntropometricaRequest request) {
        Consulta consulta = consultaRepository.findById(request.consultaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Consulta com id " + request.consultaId() + " não encontrada"
                ));
        Paciente paciente = consulta.getPaciente();
        int idade = calcularIdade(paciente, request);

        validadorProtocolo.validar(request, paciente.getSexo());
        CalculadoraAntropometrica.ResultadoCalculo resultado = calculadora.calcular(
                request,
                paciente.getSexo(),
                idade
        );

        AvaliacaoAntropometrica avaliacao = avaliacaoMapper.toEntity(request, consulta);
        preencherResultado(avaliacao, paciente, idade, resultado);

        return avaliacaoMapper.toResponse(avaliacaoRepository.save(avaliacao));
    }

    @Transactional(readOnly = true)
    public AvaliacaoAntropometricaResponse buscarPorId(Long id) {
        return avaliacaoMapper.toResponse(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoAntropometricaResponse> listarPorConsulta(Long consultaId) {
        if (!consultaRepository.existsById(consultaId)) {
            throw new RecursoNaoEncontradoException(
                    "Consulta com id " + consultaId + " não encontrada"
            );
        }
        return avaliacaoRepository.findAllByConsultaIdOrderByDataAvaliacaoDesc(consultaId).stream()
                .map(avaliacaoMapper::toResponse)
                .toList();
    }

    private AvaliacaoAntropometrica buscarEntidade(Long id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Avaliação antropométrica com id " + id + " não encontrada"
                ));
    }

    private int calcularIdade(Paciente paciente, AvaliacaoAntropometricaRequest request) {
        if (request.dataAvaliacao().toLocalDate().isBefore(paciente.getDataNascimento())) {
            throw new RegraDeNegocioException("A data da avaliação não pode ser anterior ao nascimento");
        }
        return Period.between(
                paciente.getDataNascimento(),
                request.dataAvaliacao().toLocalDate()
        ).getYears();
    }

    private void preencherResultado(
            AvaliacaoAntropometrica avaliacao,
            Paciente paciente,
            int idade,
            CalculadoraAntropometrica.ResultadoCalculo resultado
    ) {
        avaliacao.setFormulaConversao(resultado.formulaConversao());
        avaliacao.setVersaoFormula(VERSAO_FORMULA);
        avaliacao.setIdadeUtilizadaCalculo(idade);
        avaliacao.setSexoUtilizadoCalculo(paciente.getSexo());
        avaliacao.setSomaDobras(resultado.somaDobras());
        avaliacao.setDensidadeCorporal(resultado.densidadeCorporal());
        avaliacao.setImc(resultado.imc());
        avaliacao.setPercentualGordura(resultado.percentualGordura());
        avaliacao.setMassaGorda(resultado.massaGorda());
        avaliacao.setMassaMagra(resultado.massaMagra());
    }
}
