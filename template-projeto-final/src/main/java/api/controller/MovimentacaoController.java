// CAMADA: Controller
// RESPONSABILIDADE: endpoints HTTP para movimentação de animais entre piquetes.
// ROTAS ATENDIDAS: POST /movimentacoes
// SITUAÇÃO: implementada.
package api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dto.MovimentacaoRequestDTO;
import api.dto.MovimentacaoResponseDTO;
import api.service.MovimentacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/movimentacoes")
@Tag(name = "Movimentações", description = "Operações para controle de transferência de animais entre piquetes")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    public MovimentacaoController(MovimentacaoService movimentacaoService) {
        this.movimentacaoService = movimentacaoService;
    }

    @PostMapping
    @Operation(summary = "Move um boi de piquete", description = "Transfere um animal com status ATIVO para um piquete com vagas disponíveis.")
    public ResponseEntity<MovimentacaoResponseDTO> registrar(@RequestBody @Valid MovimentacaoRequestDTO dto) {
        MovimentacaoResponseDTO response = movimentacaoService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
