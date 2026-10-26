// CAMADA: Service
// RESPONSABILIDADE: gerenciamento e regras de negócio de piquetes/pastos.
// REGRAS: base para controle de ocupação e capacidade máxima em R2.
// ROTAS ATENDIDAS: POST /piquetes, GET /piquetes, GET /piquetes/{id}
// SITUAÇÃO: implementada.
package api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.dto.PiqueteRequestDTO;
import api.dto.PiqueteResponseDTO;
import api.mapper.PiqueteMapper;
import api.model.Piquete;
import api.model.StatusBoi;
import api.repository.BoiRepository;
import api.repository.PiqueteRepository;

@Service
public class PiqueteService {

    private final PiqueteRepository piqueteRepository;
    private final BoiRepository boiRepository;
    private final PiqueteMapper piqueteMapper;

    public PiqueteService(PiqueteRepository piqueteRepository, BoiRepository boiRepository, PiqueteMapper piqueteMapper) {
        this.piqueteRepository = piqueteRepository;
        this.boiRepository = boiRepository;
        this.piqueteMapper = piqueteMapper;
    }

    @Transactional
    public PiqueteResponseDTO cadastrar(PiqueteRequestDTO dto) {
        Piquete piquete = piqueteMapper.toEntity(dto);
        Piquete salvo = piqueteRepository.save(piquete);
        return piqueteMapper.toResponse(salvo, 0);
    }

    @Transactional(readOnly = true)
    public List<PiqueteResponseDTO> listar() {
        return piqueteRepository.findAll().stream()
                .map(piquete -> {
                    long ocupacao = boiRepository.countByPiqueteIdAndStatus(piquete.getId(), StatusBoi.ATIVO);
                    return piqueteMapper.toResponse(piquete, (int) ocupacao);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public PiqueteResponseDTO buscarPorId(Long id) {
        Piquete piquete = buscarEntidadePorId(id);
        long ocupacao = boiRepository.countByPiqueteIdAndStatus(piquete.getId(), StatusBoi.ATIVO);
        return piqueteMapper.toResponse(piquete, (int) ocupacao);
    }

    @Transactional(readOnly = true)
    public Piquete buscarEntidadePorId(Long id) {
        return piqueteRepository.findById(id)
                .orElseThrow(() -> new PiqueteNaoEncontradoException(id));
    }
}
