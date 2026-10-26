// CAMADA: Service
// RESPONSABILIDADE: regras de negócio para registro e consulta de pesagens de animais.
// REGRAS: R3 (apenas animal ATIVO pode ser pesado).
// ROTAS ATENDIDAS: POST /pesagens, GET /bois/{id}/pesagens
// SITUAÇÃO: implementada.
package api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.dto.PesagemRequestDTO;
import api.dto.PesagemResponseDTO;
import api.mapper.PesagemMapper;
import api.model.Boi;
import api.model.Pesagem;
import api.model.StatusBoi;
import api.repository.PesagemRepository;

@Service
public class PesagemService {

    private final PesagemRepository pesagemRepository;
    private final BoiService boiService;
    private final PesagemMapper pesagemMapper;

    public PesagemService(PesagemRepository pesagemRepository, BoiService boiService, PesagemMapper pesagemMapper) {
        this.pesagemRepository = pesagemRepository;
        this.boiService = boiService;
        this.pesagemMapper = pesagemMapper;
    }

    @Transactional
    public PesagemResponseDTO registrar(PesagemRequestDTO dto) {
        Boi boi = boiService.buscarEntidadePorId(dto.boiId());

        // R3: Só um boi com status ATIVO pode ser pesado, movimentado ou receber baixa
        if (boi.getStatus() != StatusBoi.ATIVO) {
            throw new RegraDeNegocioException("R3: Apenas bois com status ATIVO podem ser pesados. Status atual: "
                    + boi.getStatus());
        }

        Pesagem pesagem = pesagemMapper.toEntity(dto, boi);
        Pesagem salva = pesagemRepository.save(pesagem);
        return pesagemMapper.toResponse(salva);
    }

    @Transactional(readOnly = true)
    public List<PesagemResponseDTO> listarPorBoi(Long boiId) {
        // Garante que o boi existe (lança 404 se não existir)
        boiService.buscarEntidadePorId(boiId);
        List<Pesagem> pesagens = pesagemRepository.findByBoiIdOrderByDataPesagemDesc(boiId);
        return pesagemMapper.toResponseList(pesagens);
    }
}
