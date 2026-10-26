// CAMADA: Controller
// RESPONSABILIDADE: endpoints HTTP para cadastro, consulta, baixa e histórico de animais do rebanho.
// ROTAS ATENDIDAS: POST /bois, GET /bois, GET /bois/{id}, GET /bois/microchip/{codigo},
//                 PATCH /bois/{id}/baixa, GET /bois/{id}/pesagens, GET /bois/{id}/movimentacoes
// SITUAÇÃO: implementada.
package api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.dto.BaixaRequestDTO;
import api.dto.BoiRequestDTO;
import api.dto.BoiResponseDTO;
import api.dto.MovimentacaoResponseDTO;
import api.dto.PesagemResponseDTO;
import api.model.StatusBoi;
import api.service.BoiService;
import api.service.MovimentacaoService;
import api.service.PesagemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/bois")
@Tag(name = "Bois", description = "Operações para cadastro e controle de bovinos por microchip")
public class BoiController {

    private final BoiService boiService;
    private final PesagemService pesagemService;
    private final MovimentacaoService movimentacaoService;

    public BoiController(BoiService boiService, PesagemService pesagemService, MovimentacaoService movimentacaoService) {
        this.boiService = boiService;
        this.pesagemService = pesagemService;
        this.movimentacaoService = movimentacaoService;
    }

    @PostMapping
    @Operation(summary = "Cadastra um boi", description = "Cadastra um animal com número único de microchip em um piquete com vagas disponíveis.")
    public ResponseEntity<BoiResponseDTO> cadastrar(@RequestBody @Valid BoiRequestDTO dto) {
        BoiResponseDTO response = boiService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Lista os bois", description = "Lista os animais do rebanho, com opção de filtrar por status e/ou piquete.")
    public ResponseEntity<List<BoiResponseDTO>> listar(
            @RequestParam(required = false) StatusBoi status,
            @RequestParam(required = false) Long piqueteId) {
        return ResponseEntity.ok(boiService.listar(status, piqueteId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca boi por ID", description = "Retorna os detalhes de um animal específico a partir de seu ID.")
    public ResponseEntity<BoiResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(boiService.buscarPorId(id));
    }

    @GetMapping("/microchip/{codigo}")
    @Operation(summary = "Busca boi por microchip", description = "Retorna os detalhes de um animal a partir do número gravado no microchip.")
    public ResponseEntity<BoiResponseDTO> buscarPorMicrochip(@PathVariable String codigo) {
        return ResponseEntity.ok(boiService.buscarPorMicrochip(codigo));
    }

    @PatchMapping("/{id}/baixa")
    @Operation(summary = "Dá baixa em um boi", description = "Registra a baixa do animal (VENDIDO, ABATIDO ou MORTO). Venda ou abate exige histórico de pesagem.")
    public ResponseEntity<BoiResponseDTO> darBaixa(@PathVariable Long id, @RequestBody @Valid BaixaRequestDTO dto) {
        return ResponseEntity.ok(boiService.darBaixa(id, dto));
    }

    @GetMapping("/{id}/pesagens")
    @Operation(summary = "Histórico de pesagens de um boi", description = "Lista todas as pesagens já aferidas para o animal informado.")
    public ResponseEntity<List<PesagemResponseDTO>> listarPesagens(@PathVariable Long id) {
        return ResponseEntity.ok(pesagemService.listarPorBoi(id));
    }

    @GetMapping("/{id}/movimentacoes")
    @Operation(summary = "Histórico de movimentações de um boi", description = "Lista todas as transferências de piquete realizadas pelo animal informado.")
    public ResponseEntity<List<MovimentacaoResponseDTO>> listarMovimentacoes(@PathVariable Long id) {
        return ResponseEntity.ok(movimentacaoService.listarPorBoi(id));
    }
}
