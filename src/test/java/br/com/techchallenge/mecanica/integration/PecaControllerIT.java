package br.com.techchallenge.mecanica.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

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
class PecaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private String criarPecaERetornarId() throws Exception {

        String json = """
                {
                  "nome": "Filtro de Óleo",
                  "marca": "Bosch",
                  "preco": 49.90
                }
                """;

        String response = mockMvc.perform(post("/pecas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response);
        return node.get("id").textValue();
    }

    @Test
    void deveCriarPecaComSucesso() throws Exception {

        String json = """
                {
                  "nome": "Pneu",
                  "marca": "Pirelli",
                  "preco": 399.90
                }
                """;

        mockMvc.perform(post("/pecas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Pneu"))
                .andExpect(jsonPath("$.marca").value("Pirelli"))
                .andExpect(jsonPath("$.preco").value(399.90));
    }

    @Test
    void deveBuscarPecaPorIdComSucesso() throws Exception {

        String pecaId = criarPecaERetornarId();

        mockMvc.perform(get("/pecas/{id}", pecaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pecaId))
                .andExpect(jsonPath("$.nome").value("Filtro de Óleo"))
                .andExpect(jsonPath("$.marca").value("Bosch"));
    }

    @Test
    void naoDevePermitirCriarPecaDuplicada() throws Exception {

        String json = """
                {
                  "nome": "Correia Dentada",
                  "marca": "Contitech",
                  "preco": 89.90
                }
                """;

        mockMvc.perform(post("/pecas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/pecas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar404AoBuscarPecaInexistente() throws Exception {

        String idInexistente = UUID.randomUUID().toString();

        mockMvc.perform(get("/pecas/{id}", idInexistente))
                .andExpect(status().isNotFound());
    }
}