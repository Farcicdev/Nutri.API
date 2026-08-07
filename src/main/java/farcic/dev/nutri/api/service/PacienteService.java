package farcic.dev.nutri.api.service;

import farcic.dev.nutri.api.dto.request.PacienteRequest;
import farcic.dev.nutri.api.dto.response.PacienteResponse;
import farcic.dev.nutri.api.entity.Nutricionista;
import farcic.dev.nutri.api.entity.Paciente;
import farcic.dev.nutri.api.exception.RecursoNaoEncontradoException;
import farcic.dev.nutri.api.exception.RegraDeNegocioException;
import farcic.dev.nutri.api.mapper.PacienteMapper;
import farcic.dev.nutri.api.repository.NutricionistaRepository;
import farcic.dev.nutri.api.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final NutricionistaRepository nutricionistaRepository;
    private final PacienteMapper pacienteMapper;

    @Transactional
    public PacienteResponse criar(PacienteRequest request) {
        Nutricionista nutricionista = buscarNutricionista(request.nutricionistaId());
        validarNutricionistaAtivo(nutricionista);
        validarEmailUnico(request.email(), request.nutricionistaId(), null);

        Paciente paciente = pacienteMapper.toEntity(request, nutricionista);
        return pacienteMapper.toResponse(pacienteRepository.save(paciente));
    }

    @Transactional(readOnly = true)
    public List<PacienteResponse> listar() {
        return pacienteRepository.findAll().stream()
                .map(pacienteMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(Long id) {
        return pacienteMapper.toResponse(buscarEntidade(id));
    }

    @Transactional
    public PacienteResponse atualizar(Long id, PacienteRequest request) {
        Paciente paciente = buscarEntidade(id);
        Nutricionista nutricionista = buscarNutricionista(request.nutricionistaId());
        validarNutricionistaAtivo(nutricionista);
        validarEmailUnico(request.email(), request.nutricionistaId(), id);
        pacienteMapper.updateEntity(paciente, request, nutricionista);

        return pacienteMapper.toResponse(pacienteRepository.save(paciente));
    }

    @Transactional
    public PacienteResponse atualizarStatus(Long id, boolean ativo) {
        Paciente paciente = buscarEntidade(id);
        paciente.setAtivo(ativo);
        return pacienteMapper.toResponse(pacienteRepository.save(paciente));
    }

    @Transactional
    public void remover(Long id) {
        pacienteRepository.delete(buscarEntidade(id));
    }

    private Paciente buscarEntidade(Long id) {
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

    private void validarNutricionistaAtivo(Nutricionista nutricionista) {
        if (!nutricionista.isAtivo()) {
            throw new RegraDeNegocioException("Não é possível vincular um paciente a um nutricionista inativo");
        }
    }

    private void validarEmailUnico(String email, Long nutricionistaId, Long pacienteId) {
        boolean emailExistente = pacienteId == null
                ? pacienteRepository.existsByEmailIgnoreCaseAndNutricionistaId(email, nutricionistaId)
                : pacienteRepository.existsByEmailIgnoreCaseAndNutricionistaIdAndIdNot(
                        email,
                        nutricionistaId,
                        pacienteId
                );

        if (emailExistente) {
            throw new RegraDeNegocioException("Já existe um paciente com este e-mail para o nutricionista");
        }
    }
}
