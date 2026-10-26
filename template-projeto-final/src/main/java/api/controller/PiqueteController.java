// CAMADA: Controller
// RESPONSABILIDADE: endpoints HTTP para cadastro e consulta de piquetes.
// ROTAS ATENDIDAS: POST /piquetes, GET /piquetes, GET /piquetes/{id}
// SITUAÇÃO: implementada.
package api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dto.PiqueteRequestDTO;
import api.dto.PiqueteResponseDTO;
import api.service.PiqueteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/piquetes")
@Tag(name = "Piquetes", description = "Operações para gerenciamento de piquetes e pastos")
public class PiqueteController {

    private final PiqueteService piqueteService;

    public PiqueteController(PiqueteService piqueteService) {
        this.piqueteService = piqueteService;
    }

    @PostMapping
    @Operation(summary = "Cadastra um piquete", description = "Cadastra uma nova área de pasto na fazenda com sua capacidade máxima de lotação.")
    public ResponseEntity<PiqueteResponseDTO> cadastrar(@RequestBody @Valid PiqueteRequestDTO dto) {
        PiqueteResponseDTO response = piqueteService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Lista todos os piquetes", description = "Retorna todos os piquetes cadastrados juntamente com a ocupação atual de animais ativos.")
    public ResponseEntity<List<PiqueteResponseDTO>> listar() {
        return ResponseEntity.ok(piqueteService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca piquete por ID", description = "Retorna os detalhes de um piquete específico pelo seu identificador único.")
    public ResponseEntity<PiqueteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(piqueteService.buscarPorId(id));
    }
}
