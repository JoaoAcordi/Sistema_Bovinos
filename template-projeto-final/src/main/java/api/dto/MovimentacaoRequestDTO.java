// CAMADA: DTO
// RESPONSABILIDADE: dados de entrada para movimentação de um boi entre piquetes.
// SITUAÇÃO: implementada.
package api.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Schema(description = "Dados para realização de movimentação de lote/animal")
public record MovimentacaoRequestDTO(

        @Schema(description = "ID do animal a ser movimentado", example = "1")
        @NotNull(message = "O ID do boi é obrigatório")
        Long boiId,

        @Schema(description = "ID do piquete de destino", example = "2")
        @NotNull(message = "O ID do piquete de destino é obrigatório")
        Long piqueteDestinoId,

        @Schema(description = "Data em que a movimentação foi efetuada", example = "2026-10-25")
        @NotNull(message = "A data da movimentação é obrigatória")
        @PastOrPresent(message = "A data da movimentação não pode estar no futuro")
        LocalDate dataMovimentacao,

        @Schema(description = "Motivo da troca de pastagem", example = "Rotação de pasto para engorda")
        @NotBlank(message = "O motivo da movimentação é obrigatório")
        String motivo

) {
}
