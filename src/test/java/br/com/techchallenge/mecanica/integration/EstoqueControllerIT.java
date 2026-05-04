package br.com.techchallenge.mecanica.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(br.com.techchallenge.mecanica.config.SecurityTestConfig.class)
@Transactional
class EstoqueControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private String criarPecaERetornarId() throws Exception {

        String pecaJson = """
                {
                  "nome": "Pastilha de Freio",
                  "marca": "Brembo",
                  "preco": 149.90
                }
                """;

        String response = mockMvc.perform(post("/pecas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pecaJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response);
        return node.get("id").textValue();
    }

    @Test
    void deveCriarEstoqueParaPecaComSucesso() throws Exception {

        String pecaId = criarPecaERetornarId();

        String estoqueJson = """
                {
                  "pecaId": "%s",
                  "quantidade": 10
                }
                """.formatted(pecaId);

        mockMvc.perform(post("/estoque")
                .contentType(MediaType.APPLICATION_JSON)
                .content(estoqueJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.quantidade").value(10));
    }

    @Test
    void naoDevePermitirCriarEstoqueDuplicadoParaMesmaPeca() throws Exception {

        String pecaId = criarPecaERetornarId();

        String estoqueJson = """
                {
                  "pecaId": "%s",
                  "quantidade": 5
                }
                """.formatted(pecaId);

        mockMvc.perform(post("/estoque")
                .contentType(MediaType.APPLICATION_JSON)
                .content(estoqueJson))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/estoque")
                .contentType(MediaType.APPLICATION_JSON)
                .content(estoqueJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRegistrarEntradaDeEstoque() throws Exception {

        String pecaId = criarPecaERetornarId();

        mockMvc.perform(post("/estoque")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "pecaId": "%s",
                          "quantidade": 5
                        }
                        """.formatted(pecaId)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/estoque/entrada")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "pecaId": "%s",
                          "quantidade": 3
                        }
                        """.formatted(pecaId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantidade").value(8));
    }

    @Test
    void deveRegistrarSaidaDeEstoque() throws Exception {

        String pecaId = criarPecaERetornarId();

        mockMvc.perform(post("/estoque")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "pecaId": "%s",
                          "quantidade": 10
                        }
                        """.formatted(pecaId)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/estoque/saida")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "pecaId": "%s",
                          "quantidade": 4
                        }
                        """.formatted(pecaId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantidade").value(6));
    }

    @Test
    void naoDevePermitirSaidaMaiorQueEstoque() throws Exception {

        String pecaId = criarPecaERetornarId();

        mockMvc.perform(post("/estoque")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "pecaId": "%s",
                          "quantidade": 2
                        }
                        """.formatted(pecaId)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/estoque/saida")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "pecaId": "%s",
                          "quantidade": 5
                        }
                        """.formatted(pecaId)))
                .andExpect(status().isBadRequest());
    }
}