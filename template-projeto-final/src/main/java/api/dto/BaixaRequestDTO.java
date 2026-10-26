// CAMADA: DTO
// RESPONSABILIDADE: dados de entrada para dar baixa em um animal do rebanho.
// SITUAÇÃO: implementada.
package api.dto;

import api.model.StatusBoi;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para dar baixa em um boi")
public record BaixaRequestDTO(

        @Schema(description = "Novo status do animal (VENDIDO, ABATIDO ou MORTO)", example = "VENDIDO")
        @NotNull(message = "O novo status é obrigatório")
        StatusBoi novoStatus,

        @Schema(description = "Justificativa ou motivo do encerramento do ciclo do animal", example = "Venda para frigorífico parceiro")
        @NotBlank(message = "O motivo da baixa é obrigatório")
        String motivo

) {
}
