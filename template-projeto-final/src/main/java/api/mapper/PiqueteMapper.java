// CAMADA: Mapper
// RESPONSABILIDADE: conversão entre entidades Piquete e seus DTOs de entrada e saída.
// SITUAÇÃO: implementada.
package api.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import api.dto.PiqueteRequestDTO;
import api.dto.PiqueteResponseDTO;
import api.model.Piquete;

@Component
public class PiqueteMapper {

    public Piquete toEntity(PiqueteRequestDTO dto) {
        return new Piquete(dto.nome(), dto.capacidadeMaxima());
    }

    public PiqueteResponseDTO toResponse(Piquete piquete, int ocupacaoAtual) {
        return new PiqueteResponseDTO(
                piquete.getId(),
                piquete.getNome(),
                piquete.getCapacidadeMaxima(),
                ocupacaoAtual
        );
    }

    public PiqueteResponseDTO toResponse(Piquete piquete) {
        return toResponse(piquete, 0);
    }

    public List<PiqueteResponseDTO> toResponseList(List<Piquete> piquetes) {
        return piquetes.stream().map(this::toResponse).toList();
    }
}
