// CAMADA: DTO
// RESPONSABILIDADE: dados de saída com informações do piquete e sua lotação atual.
// SITUAÇÃO: implementada.
package api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de retorno de um piquete")
public record PiqueteResponseDTO(

        @Schema(description = "Identificador único do piquete", example = "1")
        Long id,

        @Schema(description = "Nome do piquete ou pasto", example = "Pasto da Sede")
        String nome,

        @Schema(description = "Capacidade máxima de animais suportada", example = "50")
        Integer capacidadeMaxima,

        @Schema(description = "Quantidade atual de animais ativos no piquete", example = "35")
        Integer ocupacaoAtual

) {
}
