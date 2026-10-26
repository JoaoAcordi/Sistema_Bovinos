// CAMADA: DTO
// RESPONSABILIDADE: dados de saída com detalhes de um boi cadastrado.
// SITUAÇÃO: implementada.
package api.dto;

import java.time.LocalDate;

import api.model.StatusBoi;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de retorno de um boi")
public record BoiResponseDTO(

        @Schema(description = "Identificador único do boi", example = "1")
        Long id,

        @Schema(description = "Número único do microchip", example = "982000123456789")
        String microchip,

        @Schema(description = "Raça do animal", example = "Nelore")
        String raca,

        @Schema(description = "Data de nascimento", example = "2024-03-15")
        LocalDate dataNascimento,

        @Schema(description = "Status do animal no rebanho", example = "ATIVO")
        StatusBoi status,

        @Schema(description = "ID do piquete em que o boi está alocado", example = "1")
        Long piqueteId,

        @Schema(description = "Nome do piquete em que o boi está alocado", example = "Pasto da Sede")
        String piqueteNome

) {
}
