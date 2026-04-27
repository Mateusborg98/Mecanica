package br.com.techchallenge.mecanica.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import br.com.techchallenge.mecanica.security.JwtService;

@SpringBootTest
@AutoConfigureMockMvc
class OrdemDeServicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveRetornar401QuandoNaoEnviarToken() throws Exception {
        mockMvc.perform(get("/ordens-servico"))
                .andExpect(status().isForbidden());
    }

    @Test
    void devePermitirAcessoComTokenValido() throws Exception {

        String token = JwtService.gerarToken("admin");

        mockMvc.perform(get("/ordens-servico")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

}
