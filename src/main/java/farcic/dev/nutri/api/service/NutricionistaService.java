package farcic.dev.nutri.api.service;

import farcic.dev.nutri.api.dto.request.NutricionistaAtualizacaoRequest;
import farcic.dev.nutri.api.dto.request.NutricionistaRequest;
import farcic.dev.nutri.api.dto.response.NutricionistaResponse;
import farcic.dev.nutri.api.entity.Nutricionista;
import farcic.dev.nutri.api.exception.RecursoNaoEncontradoException;
import farcic.dev.nutri.api.exception.RegraDeNegocioException;
import farcic.dev.nutri.api.mapper.NutricionistaMapper;
import farcic.dev.nutri.api.repository.NutricionistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NutricionistaService {

    private final NutricionistaRepository nutricionistaRepository;
    private final NutricionistaMapper nutricionistaMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public NutricionistaResponse criar(NutricionistaRequest request) {
        validarDadosUnicos(request.email(), request.crn(), null);
        Nutricionista nutricionista = nutricionistaMapper.toEntity(
                request,
                passwordEncoder.encode(request.senha())
        );
        return nutricionistaMapper.toResponse(nutricionistaRepository.save(nutricionista));
    }

    @Transactional(readOnly = true)
    public List<NutricionistaResponse> listar() {
        return nutricionistaRepository.findAll().stream()
                .map(nutricionistaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public NutricionistaResponse buscarPorId(Long id) {
        return nutricionistaMapper.toResponse(buscarEntidade(id));
    }

    @Transactional
    public NutricionistaResponse atualizar(Long id, NutricionistaAtualizacaoRequest request) {
        Nutricionista nutricionista = buscarEntidade(id);
        validarDadosUnicos(request.email(), request.crn(), id);
        nutricionistaMapper.updateEntity(nutricionista, request);

        return nutricionistaMapper.toResponse(nutricionistaRepository.save(nutricionista));
    }

    @Transactional
    public void atualizarSenha(Long id, String novaSenha) {
        Nutricionista nutricionista = buscarEntidade(id);
        nutricionista.setSenha(passwordEncoder.encode(novaSenha));
        nutricionistaRepository.save(nutricionista);
    }

    @Transactional
    public NutricionistaResponse atualizarStatus(Long id, boolean ativo) {
        Nutricionista nutricionista = buscarEntidade(id);
        nutricionista.setAtivo(ativo);
        return nutricionistaMapper.toResponse(nutricionistaRepository.save(nutricionista));
    }

    @Transactional
    public void remover(Long id) {
        nutricionistaRepository.delete(buscarEntidade(id));
    }

    private Nutricionista buscarEntidade(Long id) {
        return nutricionistaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Nutricionista com id " + id + " não encontrado"
                ));
    }

    private void validarDadosUnicos(String email, String crn, Long id) {
        boolean emailExistente = id == null
                ? nutricionistaRepository.existsByEmailIgnoreCase(email)
                : nutricionistaRepository.existsByEmailIgnoreCaseAndIdNot(email, id);

        if (emailExistente) {
            throw new RegraDeNegocioException("Já existe um nutricionista com este e-mail");
        }

        boolean crnExistente = id == null
                ? nutricionistaRepository.existsByCrnIgnoreCase(crn)
                : nutricionistaRepository.existsByCrnIgnoreCaseAndIdNot(crn, id);

        if (crnExistente) {
            throw new RegraDeNegocioException("Já existe um nutricionista com este CRN");
        }
    }

}
