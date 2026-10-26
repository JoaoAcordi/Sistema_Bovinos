// CAMADA: DTO
// RESPONSABILIDADE: dados de saída com detalhes de um registro de pesagem.
// SITUAÇÃO: implementada.
package api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de retorno de uma pesagem")
public record PesagemResponseDTO(

        @Schema(description = "Identificador único da pesagem", example = "1")
        Long id,

        @Schema(description = "ID do animal pesado", example = "1")
        Long boiId,

        @Schema(description = "Microchip do animal pesado", example = "982000123456789")
        String microchip,

        @Schema(description = "Peso aferido em kg", example = "480.50")
        BigDecimal peso,

        @Schema(description = "Data em que a pesagem ocorreu", example = "2026-10-20")
        LocalDate dataPesagem

) {
}
