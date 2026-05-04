package br.com.techchallenge.mecanica.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(br.com.techchallenge.mecanica.config.SecurityTestConfig.class)
@Transactional
class OrdemDeServicoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private String criarCliente() throws Exception {
        String json = """
                {
                  "nome": "Cliente OS",
                  "cpfCnpj": "99988877766",
                  "contato": "11911112222",
                  "email": "clienteos@email.com"
                }
                """;

        String response = mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readTree(response).get("id").textValue();
    }

    private String criarVeiculo(String clienteId) throws Exception {
        String json = """
                {
                  "placa": "OSX1A23",
                  "marca": "Toyota",
                  "modelo": "Corolla",
                  "ano": 2022,
                  "clienteId": "%s"
                }
                """.formatted(clienteId);

        String response = mockMvc.perform(post("/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readTree(response).get("id").textValue();
    }

    private String criarPeca() throws Exception {
        String json = """
                {
                  "nome": "Filtro de Ar",
                  "marca": "Bosch",
                  "preco": 79.90
                }
                """;

        String response = mockMvc.perform(post("/pecas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readTree(response).get("id").textValue();
    }

    private void criarEstoque(String pecaId, int quantidade) throws Exception {
        mockMvc.perform(post("/estoque")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "pecaId": "%s",
                          "quantidade": %d
                        }
                        """.formatted(pecaId, quantidade)))
                .andExpect(status().isCreated());
    }

    private String criarOrdemServico(String clienteId, String veiculoId) throws Exception {
        String json = """
                {
                  "clienteId": "%s",
                  "veiculoId": "%s"
                }
                """.formatted(clienteId, veiculoId);

        String response = mockMvc.perform(post("/ordens-servico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readTree(response).get("id").textValue();
    }

    @Test
    void fluxoCompletoDaOrdemDeServicoComSucesso() throws Exception {

        String clienteId = criarCliente();
        String veiculoId = criarVeiculo(clienteId);
        String pecaId = criarPeca();
        criarEstoque(pecaId, 10);

        String osId = criarOrdemServico(clienteId, veiculoId);

        mockMvc.perform(post("/ordens-servico/{id}/pecas", osId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "pecaId": "%s",
                          "quantidade": 2
                        }
                        """.formatted(pecaId)))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/ordens-servico/{id}/status", osId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "status": "EM_DIAGNOSTICO" }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("EM_DIAGNOSTICO"));

        mockMvc.perform(patch("/ordens-servico/{id}/status", osId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "status": "EM_EXECUCAO" }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("EM_EXECUCAO"));

        mockMvc.perform(patch("/ordens-servico/{id}/status", osId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "status": "FINALIZADA" }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FINALIZADA"));
    }

    @Test
    void naoDevePermitirAdicionarPecaSemEstoqueSuficiente() throws Exception {

        String clienteId = criarCliente();
        String veiculoId = criarVeiculo(clienteId);
        String pecaId = criarPeca();

        criarEstoque(pecaId, 1);

        String osId = criarOrdemServico(clienteId, veiculoId);

        mockMvc.perform(post("/ordens-servico/{id}/pecas", osId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "pecaId": "%s",
                          "quantidade": 5
                        }
                        """.formatted(pecaId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDevePermitirTransicaoDeStatusInvalida() throws Exception {

        String clienteId = criarCliente();
        String veiculoId = criarVeiculo(clienteId);

        String osId = criarOrdemServico(clienteId, veiculoId);

        mockMvc.perform(patch("/ordens-servico/{id}/status", osId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "status": "FINALIZADA" }
                        """))
                .andExpect(status().isBadRequest());
    }
}