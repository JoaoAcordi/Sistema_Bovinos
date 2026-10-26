// CAMADA: Controller
// RESPONSABILIDADE: endpoints HTTP para registro de pesagens de animais do rebanho.
// ROTAS ATENDIDAS: POST /pesagens
// SITUAÇÃO: implementada.
package api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dto.PesagemRequestDTO;
import api.dto.PesagemResponseDTO;
import api.service.PesagemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pesagens")
@Tag(name = "Pesagens", description = "Operações para aferição e registro de peso de animais")
public class PesagemController {

    private final PesagemService pesagemService;

    public PesagemController(PesagemService pesagemService) {
        this.pesagemService = pesagemService;
    }

    @PostMapping
    @Operation(summary = "Registra pesagem de um boi", description = "Salva o peso aferido para um animal com status ATIVO.")
    public ResponseEntity<PesagemResponseDTO> registrar(@RequestBody @Valid PesagemRequestDTO dto) {
        PesagemResponseDTO response = pesagemService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
