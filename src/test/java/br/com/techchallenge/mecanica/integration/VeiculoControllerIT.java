package br.com.techchallenge.mecanica.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class VeiculoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private String criarClienteERetornarId() throws Exception {
        String clienteJson = """
                {
                  "nome": "Cliente Teste",
                  "cpfCnpj": "55566677788",
                  "contato": "11999998888",
                  "email": "cliente@teste.com"
                }
                """;

        String response = mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response);
        return node.get("id").textValue();
    }

    @Test
    void deveCriarVeiculoComSucesso() throws Exception {

        String clienteId = criarClienteERetornarId();

        String veiculoJson = """
                {
                  "placa": "ABC1D23",
                  "marca": "Toyota",
                  "modelo": "Corolla",
                  "ano": 2022,
                  "clienteId": "%s"
                }
                """.formatted(clienteId);

        mockMvc.perform(post("/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(veiculoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.placa").value("ABC1D23"))
                .andExpect(jsonPath("$.marca").value("Toyota"));
    }

    @Test
    void deveBuscarVeiculoPorPlaca() throws Exception {

        String clienteId = criarClienteERetornarId();

        String placa = "XYZ9A88";

        String veiculoJson = """
                {
                  "placa": "XYZ9A88",
                  "marca": "Honda",
                  "modelo": "Civic",
                  "ano": 2021,
                  "clienteId": "%s"
                }
                """.formatted(clienteId);

        mockMvc.perform(post("/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(veiculoJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/veiculos/placa/{placa}", placa))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.placa").value(placa))
                .andExpect(jsonPath("$.modelo").value("Civic"));
    }

    @Test
    void naoDevePermitirCriarVeiculoComPlacaDuplicada() throws Exception {

        String clienteId = criarClienteERetornarId();

        String veiculoJson = """
                {
                  "placa": "DUP1A23",
                  "marca": "Ford",
                  "modelo": "Focus",
                  "ano": 2020,
                  "clienteId": "%s"
                }
                """.formatted(clienteId);

        mockMvc.perform(post("/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(veiculoJson))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(veiculoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar404AoBuscarVeiculoInexistente() throws Exception {

        mockMvc.perform(get("/veiculos/placa/{placa}", "ZZZ9Z99"))
                .andExpect(status().isNotFound());
    }

}