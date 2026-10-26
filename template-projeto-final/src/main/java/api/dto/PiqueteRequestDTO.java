// CAMADA: DTO
// RESPONSABILIDADE: dados de entrada para cadastro de um piquete.
// SITUAÇÃO: implementada.
package api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para cadastro de piquete")
public record PiqueteRequestDTO(

        @Schema(description = "Nome do piquete ou pasto", example = "Pasto da Sede")
        @NotBlank(message = "O nome do piquete é obrigatório")
        String nome,

        @Schema(description = "Capacidade máxima de animais suportada", example = "50")
        @NotNull(message = "A capacidade máxima é obrigatória")
        @Min(value = 1, message = "A capacidade máxima deve ser de no mínimo 1")
        Integer capacidadeMaxima

) {
}
