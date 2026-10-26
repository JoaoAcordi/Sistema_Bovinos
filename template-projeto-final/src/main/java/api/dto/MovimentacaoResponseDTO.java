// CAMADA: DTO
// RESPONSABILIDADE: dados de saída com detalhes de uma movimentação entre piquetes.
// SITUAÇÃO: implementada.
package api.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de retorno de uma movimentação entre piquetes")
public record MovimentacaoResponseDTO(

        @Schema(description = "Identificador único da movimentação", example = "1")
        Long id,

        @Schema(description = "ID do animal movimentado", example = "1")
        Long boiId,

        @Schema(description = "Número do microchip do animal", example = "982000123456789")
        String microchip,

        @Schema(description = "ID do piquete de origem", example = "1")
        Long piqueteOrigemId,

        @Schema(description = "Nome do piquete de origem", example = "Pasto da Sede")
        String piqueteOrigemNome,

        @Schema(description = "ID do piquete de destino", example = "2")
        Long piqueteDestinoId,

        @Schema(description = "Nome do piquete de destino", example = "Piquete de Engorda A")
        String piqueteDestinoNome,

        @Schema(description = "Data da movimentação", example = "2026-10-25")
        LocalDate dataMovimentacao,

        @Schema(description = "Motivo da transferência", example = "Rotação de pasto para engorda")
        String motivo

) {
}
