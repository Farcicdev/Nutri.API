package farcic.dev.nutri.api.service;

import farcic.dev.nutri.api.dto.request.ConsultaRequest;
import farcic.dev.nutri.api.dto.response.ConsultaResponse;
import farcic.dev.nutri.api.entity.Consulta;
import farcic.dev.nutri.api.entity.Nutricionista;
import farcic.dev.nutri.api.entity.Paciente;
import farcic.dev.nutri.api.entity.enums.StatusConsulta;
import farcic.dev.nutri.api.exception.RecursoNaoEncontradoException;
import farcic.dev.nutri.api.exception.RegraDeNegocioException;
import farcic.dev.nutri.api.mapper.ConsultaMapper;
import farcic.dev.nutri.api.repository.ConsultaRepository;
import farcic.dev.nutri.api.repository.NutricionistaRepository;
import farcic.dev.nutri.api.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final NutricionistaRepository nutricionistaRepository;
    private final ConsultaMapper consultaMapper;

    @Transactional
    public ConsultaResponse criar(ConsultaRequest request) {
        Paciente paciente = buscarPaciente(request.pacienteId());
        Nutricionista nutricionista = buscarNutricionista(request.nutricionistaId());
        validarVinculosAtivos(paciente, nutricionista);
        validarPacienteDoNutricionista(paciente, nutricionista);

        Consulta consulta = consultaMapper.toEntity(request, paciente, nutricionista);
        return consultaMapper.toResponse(consultaRepository.save(consulta));
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponse> listar() {
        return consultaRepository.findAllByOrderByDataConsultaDesc().stream()
                .map(consultaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConsultaResponse buscarPorId(Long id) {
        return consultaMapper.toResponse(buscarEntidade(id));
    }

    @Transactional
    public ConsultaResponse atualizar(Long id, ConsultaRequest request) {
        Consulta consulta = buscarEntidade(id);
        Paciente paciente = buscarPaciente(request.pacienteId());
        Nutricionista nutricionista = buscarNutricionista(request.nutricionistaId());
        validarVinculosAtivos(paciente, nutricionista);
        validarPacienteDoNutricionista(paciente, nutricionista);

        consultaMapper.updateEntity(consulta, request, paciente, nutricionista);
        return consultaMapper.toResponse(consultaRepository.save(consulta));
    }

    @Transactional
    public ConsultaResponse atualizarStatus(Long id, StatusConsulta status) {
        Consulta consulta = buscarEntidade(id);
        consulta.setStatus(status);
        return consultaMapper.toResponse(consultaRepository.save(consulta));
    }

    @Transactional
    public void remover(Long id) {
        consultaRepository.delete(buscarEntidade(id));
    }

    private Consulta buscarEntidade(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Consulta com id " + id + " não encontrada"
                ));
    }

    private Paciente buscarPaciente(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Paciente com id " + id + " não encontrado"
                ));
    }

    private Nutricionista buscarNutricionista(Long id) {
        return nutricionistaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Nutricionista com id " + id + " não encontrado"
                ));
    }

    private void validarVinculosAtivos(Paciente paciente, Nutricionista nutricionista) {
        if (!paciente.isAtivo()) {
            throw new RegraDeNegocioException("Não é possível agendar consulta para paciente inativo");
        }
        if (!nutricionista.isAtivo()) {
            throw new RegraDeNegocioException("Não é possível agendar consulta com nutricionista inativo");
        }
    }

    private void validarPacienteDoNutricionista(Paciente paciente, Nutricionista nutricionista) {
        if (!paciente.getNutricionista().getId().equals(nutricionista.getId())) {
            throw new RegraDeNegocioException("O paciente não pertence ao nutricionista informado");
        }
    }
}
