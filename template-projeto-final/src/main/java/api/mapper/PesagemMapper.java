// CAMADA: Mapper
// RESPONSABILIDADE: conversão entre entidades Pesagem e seus DTOs de entrada e saída.
// SITUAÇÃO: implementada.
package api.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import api.dto.PesagemRequestDTO;
import api.dto.PesagemResponseDTO;
import api.model.Boi;
import api.model.Pesagem;

@Component
public class PesagemMapper {

    public Pesagem toEntity(PesagemRequestDTO dto, Boi boi) {
        return new Pesagem(boi, dto.peso(), dto.dataPesagem());
    }

    public PesagemResponseDTO toResponse(Pesagem pesagem) {
        return new PesagemResponseDTO(
                pesagem.getId(),
                pesagem.getBoi().getId(),
                pesagem.getBoi().getMicrochip(),
                pesagem.getPeso(),
                pesagem.getDataPesagem()
        );
    }

    public List<PesagemResponseDTO> toResponseList(List<Pesagem> pesagens) {
        return pesagens.stream().map(this::toResponse).toList();
    }
}
