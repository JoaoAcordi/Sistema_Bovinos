// CAMADA: Mapper
// RESPONSABILIDADE: conversão entre entidades Movimentacao e seus DTOs de entrada e saída.
// SITUAÇÃO: implementada.
package api.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import api.dto.MovimentacaoRequestDTO;
import api.dto.MovimentacaoResponseDTO;
import api.model.Boi;
import api.model.Movimentacao;
import api.model.Piquete;

@Component
public class MovimentacaoMapper {

    public Movimentacao toEntity(Boi boi, Piquete origem, Piquete destino, MovimentacaoRequestDTO dto) {
        return new Movimentacao(
                boi,
                origem,
                destino,
                dto.dataMovimentacao(),
                dto.motivo()
        );
    }

    public MovimentacaoResponseDTO toResponse(Movimentacao mov) {
        return new MovimentacaoResponseDTO(
                mov.getId(),
                mov.getBoi().getId(),
                mov.getBoi().getMicrochip(),
                mov.getPiqueteOrigem().getId(),
                mov.getPiqueteOrigem().getNome(),
                mov.getPiqueteDestino().getId(),
                mov.getPiqueteDestino().getNome(),
                mov.getDataMovimentacao(),
                mov.getMotivo()
        );
    }

    public List<MovimentacaoResponseDTO> toResponseList(List<Movimentacao> movimentacoes) {
        return movimentacoes.stream().map(this::toResponse).toList();
    }
}
