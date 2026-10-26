// CAMADA: DTO
// RESPONSABILIDADE: dados de entrada para cadastro de um boi.
// SITUAÇÃO: implementada.
package api.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Schema(description = "Dados para cadastro de boi")
public record BoiRequestDTO(

        @Schema(description = "Número único do microchip do boi", example = "982000123456789")
        @NotBlank(message = "O microchip é obrigatório")
        String microchip,

        @Schema(description = "Raça do animal", example = "Nelore")
        @NotBlank(message = "A raça é obrigatória")
        String raca,

        @Schema(description = "Data de nascimento do animal", example = "2024-03-15")
        @NotNull(message = "A data de nascimento é obrigatória")
        @PastOrPresent(message = "A data de nascimento não pode estar no futuro")
        LocalDate dataNascimento,

        @Schema(description = "ID do piquete inicial onde o animal ficará alocado", example = "1")
        @NotNull(message = "O ID do piquete é obrigatório")
        Long piqueteId

) {
}
