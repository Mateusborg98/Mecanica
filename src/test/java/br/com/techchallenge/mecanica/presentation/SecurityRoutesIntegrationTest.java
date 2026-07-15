package br.com.techchallenge.mecanica.presentation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import br.com.techchallenge.mecanica.infrastructure.security.JwtService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityRoutesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    @Qualifier("requestMappingHandlerMapping")
    private RequestMappingHandlerMapping handlerMapping;

    @Test
    void acompanhamentoDaOrdemDeveSerPublico() throws Exception {
        mockMvc.perform(get("/ordens-servico/acompanhamento/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void contratoDeRotasDeveUsarPatchParaInativacaoERemoverConsultaDuplicadaDaOrdem() {
        var mappings = handlerMapping.getHandlerMethods().keySet();

        assertTrue(temRota(mappings, "/veiculos/{id}/inativar", RequestMethod.PATCH));
        assertTrue(temRota(mappings, "/pecas/{id}/inativar", RequestMethod.PATCH));
        assertTrue(temRota(mappings, "/servicos/{id}/inativar", RequestMethod.PATCH));
        assertFalse(temRota(mappings, "/veiculos/{id}", RequestMethod.DELETE));
        assertFalse(temRota(mappings, "/pecas/{id}", RequestMethod.DELETE));
        assertFalse(temRota(mappings, "/servicos/{id}", RequestMethod.DELETE));
        assertFalse(temRota(mappings, "/ordens-servico/{id}", RequestMethod.GET));
        assertTrue(temRota(mappings, "/ordens-servico/acompanhamento/{id}", RequestMethod.GET));
    }

    @Test
    void estoqueDeveExistirEContinuarProtegido() throws Exception {
        mockMvc.perform(get("/estoques"))
                .andExpect(status().isForbidden());

        String token = jwtService.gerarToken("1", "ADMIN");
        mockMvc.perform(get("/estoques").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void rotaAguardarAprovacaoDeveExistirEExigirToken() throws Exception {
        var id = UUID.randomUUID();
        mockMvc.perform(post("/ordens-servico/{id}/aguardar-aprovacao", id))
                .andExpect(status().isForbidden());

        String token = jwtService.gerarToken("1", "ADMIN");
        mockMvc.perform(post("/ordens-servico/{id}/aguardar-aprovacao", id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void callbacksExternosDeAprovacaoERecusaDevemSerPublicos() throws Exception {
        var id = UUID.randomUUID();

        mockMvc.perform(post("/ordens-servico/{id}/aprovar-orcamento", id))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/ordens-servico/{id}/negar-orcamento", id))
                .andExpect(status().isNotFound());
    }

    private boolean temRota(
            java.util.Set<org.springframework.web.servlet.mvc.method.RequestMappingInfo> mappings,
            String caminho,
            RequestMethod metodo) {
        return mappings.stream().anyMatch(mapping ->
                mapping.getPatternValues().contains(caminho)
                        && mapping.getMethodsCondition().getMethods().contains(metodo));
    }
}
