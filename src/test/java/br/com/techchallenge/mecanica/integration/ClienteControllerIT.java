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

import br.com.techchallenge.mecanica.config.SecurityTestConfig;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(SecurityTestConfig.class)
@Transactional
class ClienteControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveCriarClienteComSucesso() throws Exception {

        String json = """
                {
                  "nome": "João da Silva",
                  "cpfCnpj": "12345678909",
                  "contato": "11999999999",
                  "email": "joao@email.com"
                }
                """;

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("João da Silva"))
                .andExpect(jsonPath("$.cpfCnpj").value("12345678909"));
    }

    @Test
    void naoDeveCriarClienteComCpfDuplicado() throws Exception {

        String json = """
                {
                  "nome": "João da Silva",
                  "cpfCnpj": "12345678909",
                  "contato": "11999999999",
                  "email": "joao@email.com"
                }
                """;

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveBuscarClientePorIdComSucesso() throws Exception {

        String json = """
                {
                  "nome": "Maria Oliveira",
                  "cpfCnpj": "98765432100",
                  "contato": "11988887777",
                  "email": "maria@email.com"
                }
                """;

        String response = mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);
        String clienteId = jsonNode.get("id").asString();

        mockMvc.perform(get("/clientes/{id}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clienteId))
                .andExpect(jsonPath("$.nome").value("Maria Oliveira"))
                .andExpect(jsonPath("$.cpfCnpj").value("98765432100"));
    }

    @Test
    void deveRetornar404AoBuscarClienteInexistente() throws Exception {

        String idInexistente = UUID.randomUUID().toString();

        mockMvc.perform(get("/clientes/{id}", idInexistente))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveBuscarClientePorCpfCnpjComSucesso() throws Exception {

        String cpfCnpj = "11122233344";

        String json = """
                {
                  "nome": "Carlos Pereira",
                  "cpfCnpj": "11122233344",
                  "contato": "11977776666",
                  "email": "carlos@email.com"
                }
                """;

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/clientes/cpf-cnpj/{cpfCnpj}", cpfCnpj))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos Pereira"))
                .andExpect(jsonPath("$.cpfCnpj").value(cpfCnpj));
    }

    @Test
    void deveRetornar404AoBuscarClientePorCpfCnpjInexistente() throws Exception {

        String cpfInexistente = "00000000000";

        mockMvc.perform(get("/clientes/cpf-cnpj/{cpfCnpj}", cpfInexistente))
                .andExpect(status().isNotFound());
    }
}
