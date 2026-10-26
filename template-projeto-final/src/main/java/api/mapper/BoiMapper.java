// CAMADA: Mapper
// RESPONSABILIDADE: conversão entre entidades Boi e seus DTOs de entrada e saída.
// SITUAÇÃO: implementada.
package api.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import api.dto.BoiRequestDTO;
import api.dto.BoiResponseDTO;
import api.model.Boi;
import api.model.Piquete;
import api.model.StatusBoi;

@Component
public class BoiMapper {

    public Boi toEntity(BoiRequestDTO dto, Piquete piquete) {
        return new Boi(
                dto.microchip(),
                dto.raca(),
                dto.dataNascimento(),
                StatusBoi.ATIVO,
                piquete
        );
    }

    public BoiResponseDTO toResponse(Boi boi) {
        Long piqueteId = boi.getPiquete() != null ? boi.getPiquete().getId() : null;
        String piqueteNome = boi.getPiquete() != null ? boi.getPiquete().getNome() : null;

        return new BoiResponseDTO(
                boi.getId(),
                boi.getMicrochip(),
                boi.getRaca(),
                boi.getDataNascimento(),
                boi.getStatus(),
                piqueteId,
                piqueteNome
        );
    }

    public List<BoiResponseDTO> toResponseList(List<Boi> bois) {
        return bois.stream().map(this::toResponse).toList();
    }
}
