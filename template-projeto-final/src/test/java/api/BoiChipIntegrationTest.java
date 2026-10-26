package api;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import api.dto.BaixaRequestDTO;
import api.dto.BoiRequestDTO;
import api.dto.MovimentacaoRequestDTO;
import api.dto.PesagemRequestDTO;
import api.dto.PiqueteRequestDTO;
import api.model.StatusBoi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
class BoiChipIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Fluxo completo de cadastro, pesagem, movimentação e baixa com regras R1, R2, R3 e R4")
    void fluxoCompletoTest() throws Exception {
        // 1. Cadastra Piquete 1 com capacidade 1
        PiqueteRequestDTO piquete1DTO = new PiqueteRequestDTO("Pasto da Sede", 1);
        String piquete1Json = mockMvc.perform(post("/piquetes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(piquete1DTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Pasto da Sede"))
                .andExpect(jsonPath("$.capacidadeMaxima").value(1))
                .andReturn().getResponse().getContentAsString();
        Long piquete1Id = objectMapper.readTree(piquete1Json).get("id").asLong();

        // 2. Cadastra Piquete 2 com capacidade 2
        PiqueteRequestDTO piquete2DTO = new PiqueteRequestDTO("Pasto dos Fundos", 2);
        String piquete2Json = mockMvc.perform(post("/piquetes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(piquete2DTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long piquete2Id = objectMapper.readTree(piquete2Json).get("id").asLong();

        // 3. Cadastra Boi 1 no Piquete 1
        BoiRequestDTO boi1DTO = new BoiRequestDTO("982000000000001", "Nelore", LocalDate.of(2024, 1, 15), piquete1Id);
        String boi1Json = mockMvc.perform(post("/bois")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boi1DTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.microchip").value("982000000000001"))
                .andExpect(jsonPath("$.status").value("ATIVO"))
                .andReturn().getResponse().getContentAsString();
        Long boi1Id = objectMapper.readTree(boi1Json).get("id").asLong();

        // 4. Teste R1: Tentar cadastrar boi com mesmo microchip deve retornar 400
        BoiRequestDTO boiDuplicado = new BoiRequestDTO("982000000000001", "Angus", LocalDate.of(2024, 2, 1), piquete2Id);
        mockMvc.perform(post("/bois")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boiDuplicado)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value(org.hamcrest.Matchers.containsString("R1")));

        // 5. Teste R2: Tentar cadastrar outro boi no Piquete 1 (capacidade 1, já lotado) deve retornar 400
        BoiRequestDTO boiLotacao = new BoiRequestDTO("982000000000002", "Angus", LocalDate.of(2024, 2, 1), piquete1Id);
        mockMvc.perform(post("/bois")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boiLotacao)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value(org.hamcrest.Matchers.containsString("R2")));

        // 6. Teste R4: Tentar dar baixa como VENDIDO sem pesagem prévia deve retornar 400
        BaixaRequestDTO baixaSemPesagem = new BaixaRequestDTO(StatusBoi.VENDIDO, "Venda teste sem peso");
        mockMvc.perform(patch("/bois/" + boi1Id + "/baixa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(baixaSemPesagem)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value(org.hamcrest.Matchers.containsString("R4")));

        // 7. Registra pesagem com sucesso
        PesagemRequestDTO pesagemDTO = new PesagemRequestDTO(boi1Id, new BigDecimal("450.50"), LocalDate.now());
        mockMvc.perform(post("/pesagens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pesagemDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.peso").value(450.50));

        // Consulta histórico de pesagens
        mockMvc.perform(get("/bois/" + boi1Id + "/pesagens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].peso").value(450.50));

        // 8. Move boi do Piquete 1 para Piquete 2
        MovimentacaoRequestDTO movDTO = new MovimentacaoRequestDTO(boi1Id, piquete2Id, LocalDate.now(), "Troca de pasto");
        mockMvc.perform(post("/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.piqueteOrigemNome").value("Pasto da Sede"))
                .andExpect(jsonPath("$.piqueteDestinoNome").value("Pasto dos Fundos"));

        // 9. Agora que tem pesagem, dá baixa como VENDIDO
        BaixaRequestDTO baixaValida = new BaixaRequestDTO(StatusBoi.VENDIDO, "Venda para frigorífico");
        mockMvc.perform(patch("/bois/" + boi1Id + "/baixa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(baixaValida)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("VENDIDO"));

        // 10. Teste R3: Tentar registrar pesagem em boi que não é mais ATIVO deve retornar 400
        PesagemRequestDTO pesagemInativo = new PesagemRequestDTO(boi1Id, new BigDecimal("460.00"), LocalDate.now());
        mockMvc.perform(post("/pesagens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pesagemInativo)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value(org.hamcrest.Matchers.containsString("R3")));

        // 11. Teste R3: Tentar movimentar boi que não é mais ATIVO deve retornar 400
        MovimentacaoRequestDTO movInativo = new MovimentacaoRequestDTO(boi1Id, piquete1Id, LocalDate.now(), "Mover vendido");
        mockMvc.perform(post("/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movInativo)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value(org.hamcrest.Matchers.containsString("R3")));

        // 12. Teste busca por microchip
        mockMvc.perform(get("/bois/microchip/982000000000001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.microchip").value("982000000000001"))
                .andExpect(jsonPath("$.status").value("VENDIDO"));

        // 13. Teste 404 para ID inexistente
        mockMvc.perform(get("/bois/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem").value(org.hamcrest.Matchers.containsString("não encontrado")));
    }
}
