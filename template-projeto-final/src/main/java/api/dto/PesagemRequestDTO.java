// CAMADA: DTO
// RESPONSABILIDADE: dados de entrada para registrar a pesagem de um animal.
// SITUAÇÃO: implementada.
package api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

@Schema(description = "Dados para registro de pesagem")
public record PesagemRequestDTO(

        @Schema(description = "ID do animal que foi pesado", example = "1")
        @NotNull(message = "O ID do boi é obrigatório")
        Long boiId,

        @Schema(description = "Peso aferido em kg", example = "480.50")
        @NotNull(message = "O peso é obrigatório")
        @Positive(message = "O peso deve ser maior que zero")
        BigDecimal peso,

        @Schema(description = "Data em que a pesagem foi realizada", example = "2026-10-20")
        @NotNull(message = "A data da pesagem é obrigatória")
        @PastOrPresent(message = "A data da pesagem não pode estar no futuro")
        LocalDate dataPesagem

) {
}
