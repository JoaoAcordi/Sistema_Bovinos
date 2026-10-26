// CAMADA: Service
// RESPONSABILIDADE: regras de negócio para transferência de animais entre piquetes.
// REGRAS: R2 (respeito à capacidade máxima do piquete destino) e
//         R3 (apenas animal ATIVO pode ser movimentado).
// ROTAS ATENDIDAS: POST /movimentacoes, GET /bois/{id}/movimentacoes
// SITUAÇÃO: implementada.
package api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.dto.MovimentacaoRequestDTO;
import api.dto.MovimentacaoResponseDTO;
import api.mapper.MovimentacaoMapper;
import api.model.Boi;
import api.model.Movimentacao;
import api.model.Piquete;
import api.model.StatusBoi;
import api.repository.BoiRepository;
import api.repository.MovimentacaoRepository;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final BoiRepository boiRepository;
    private final BoiService boiService;
    private final PiqueteService piqueteService;
    private final MovimentacaoMapper movimentacaoMapper;

    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository, BoiRepository boiRepository,
                               BoiService boiService, PiqueteService piqueteService,
                               MovimentacaoMapper movimentacaoMapper) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.boiRepository = boiRepository;
        this.boiService = boiService;
        this.piqueteService = piqueteService;
        this.movimentacaoMapper = movimentacaoMapper;
    }

    @Transactional
    public MovimentacaoResponseDTO registrar(MovimentacaoRequestDTO dto) {
        Boi boi = boiService.buscarEntidadePorId(dto.boiId());

        // R3: Só um boi com status ATIVO pode ser pesado, movimentado ou receber baixa
        if (boi.getStatus() != StatusBoi.ATIVO) {
            throw new RegraDeNegocioException("R3: Apenas bois com status ATIVO podem ser movimentados. Status atual: "
                    + boi.getStatus());
        }

        Piquete origem = boi.getPiquete();
        Piquete destino = piqueteService.buscarEntidadePorId(dto.piqueteDestinoId());

        if (origem.getId().equals(destino.getId())) {
            throw new RegraDeNegocioException("O boi já se encontra no piquete informado como destino: " + destino.getNome());
        }

        // R2: Um piquete não pode receber mais bois ativos do que a sua capacidade máxima
        long ativosNoDestino = boiRepository.countByPiqueteIdAndStatus(destino.getId(), StatusBoi.ATIVO);
        if (ativosNoDestino >= destino.getCapacidadeMaxima()) {
            throw new RegraDeNegocioException("R2: o piquete " + destino.getNome() + " já está na capacidade máxima ("
                    + destino.getCapacidadeMaxima() + ")");
        }

        // Move o animal para o piquete de destino
        boi.setPiquete(destino);
        boiRepository.save(boi);

        // Registra o evento de movimentação no histórico
        Movimentacao mov = movimentacaoMapper.toEntity(boi, origem, destino, dto);
        Movimentacao salva = movimentacaoRepository.save(mov);

        return movimentacaoMapper.toResponse(salva);
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoResponseDTO> listarPorBoi(Long boiId) {
        // Garante que o boi existe (lança 404 se não existir)
        boiService.buscarEntidadePorId(boiId);
        List<Movimentacao> movimentacoes = movimentacaoRepository.findByBoiIdOrderByDataMovimentacaoDesc(boiId);
        return movimentacaoMapper.toResponseList(movimentacoes);
    }
}
