// CAMADA: Service
// RESPONSABILIDADE: regras de negócio e controle do rebanho bovino.
// REGRAS: R1 (microchip único), R2 (capacidade máxima do piquete),
//         R3 (apenas animal ATIVO recebe baixa) e R4 (baixa VENDIDO/ABATIDO exige pesagem).
// ROTAS ATENDIDAS: POST /bois, GET /bois, GET /bois/{id}, GET /bois/microchip/{codigo}, PATCH /bois/{id}/baixa
// SITUAÇÃO: implementada.
package api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.dto.BaixaRequestDTO;
import api.dto.BoiRequestDTO;
import api.dto.BoiResponseDTO;
import api.mapper.BoiMapper;
import api.model.Boi;
import api.model.Piquete;
import api.model.StatusBoi;
import api.repository.BoiRepository;
import api.repository.PesagemRepository;

@Service
public class BoiService {

    private final BoiRepository boiRepository;
    private final PiqueteService piqueteService;
    private final PesagemRepository pesagemRepository;
    private final BoiMapper boiMapper;

    public BoiService(BoiRepository boiRepository, PiqueteService piqueteService,
                      PesagemRepository pesagemRepository, BoiMapper boiMapper) {
        this.boiRepository = boiRepository;
        this.piqueteService = piqueteService;
        this.pesagemRepository = pesagemRepository;
        this.boiMapper = boiMapper;
    }

    @Transactional
    public BoiResponseDTO cadastrar(BoiRequestDTO dto) {
        // R1: Não pode existir dois bois com o mesmo número de microchip
        if (boiRepository.existsByMicrochip(dto.microchip())) {
            throw new RegraDeNegocioException("R1: Já existe um boi cadastrado com o microchip " + dto.microchip());
        }

        Piquete piquete = piqueteService.buscarEntidadePorId(dto.piqueteId());

        // R2: Um piquete não pode receber mais bois ativos do que a sua capacidade máxima
        long ativosNoPiquete = boiRepository.countByPiqueteIdAndStatus(piquete.getId(), StatusBoi.ATIVO);
        if (ativosNoPiquete >= piquete.getCapacidadeMaxima()) {
            throw new RegraDeNegocioException("R2: o piquete " + piquete.getNome() + " já está na capacidade máxima ("
                    + piquete.getCapacidadeMaxima() + ")");
        }

        Boi boi = boiMapper.toEntity(dto, piquete);
        Boi salvo = boiRepository.save(boi);
        return boiMapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public List<BoiResponseDTO> listar(StatusBoi status, Long piqueteId) {
        List<Boi> resultado;

        if (status != null && piqueteId != null) {
            resultado = boiRepository.findByPiqueteIdAndStatus(piqueteId, status);
        } else if (status != null) {
            resultado = boiRepository.findByStatus(status);
        } else if (piqueteId != null) {
            resultado = boiRepository.findByPiqueteId(piqueteId);
        } else {
            resultado = boiRepository.findAll();
        }

        return boiMapper.toResponseList(resultado);
    }

    @Transactional(readOnly = true)
    public BoiResponseDTO buscarPorId(Long id) {
        return boiMapper.toResponse(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public BoiResponseDTO buscarPorMicrochip(String microchip) {
        Boi boi = boiRepository.findByMicrochip(microchip)
                .orElseThrow(() -> new BoiNaoEncontradoException(microchip));
        return boiMapper.toResponse(boi);
    }

    @Transactional
    public BoiResponseDTO darBaixa(Long id, BaixaRequestDTO dto) {
        Boi boi = buscarEntidadePorId(id);

        // R3: Só um boi com status ATIVO pode ser pesado, movimentado ou receber baixa
        if (boi.getStatus() != StatusBoi.ATIVO) {
            throw new RegraDeNegocioException("R3: Apenas bois com status ATIVO podem receber baixa. Status atual: "
                    + boi.getStatus());
        }

        if (dto.novoStatus() == StatusBoi.ATIVO) {
            throw new RegraDeNegocioException("O novo status de baixa deve ser VENDIDO, ABATIDO ou MORTO");
        }

        // R4: Um boi só pode ser baixado como VENDIDO ou ABATIDO se tiver ao menos uma pesagem registrada
        if (dto.novoStatus() == StatusBoi.VENDIDO || dto.novoStatus() == StatusBoi.ABATIDO) {
            long totalPesagens = pesagemRepository.countByBoiId(boi.getId());
            if (totalPesagens == 0) {
                throw new RegraDeNegocioException("R4: Um boi só pode ser baixado como " + dto.novoStatus()
                        + " se tiver ao menos uma pesagem registrada");
            }
        }

        boi.setStatus(dto.novoStatus());
        Boi atualizado = boiRepository.save(boi);
        return boiMapper.toResponse(atualizado);
    }

    @Transactional(readOnly = true)
    public Boi buscarEntidadePorId(Long id) {
        return boiRepository.findById(id)
                .orElseThrow(() -> new BoiNaoEncontradoException(id));
    }
}
