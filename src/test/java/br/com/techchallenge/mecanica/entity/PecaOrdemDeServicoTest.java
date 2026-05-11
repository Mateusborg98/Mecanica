package br.com.techchallenge.mecanica.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class PecaOrdemDeServicoTest {

    @Test
    void pecasComMesmoIdDevemSerIguais() {
        UUID id = UUID.randomUUID();

        PecaOrdemDeServico item1 = new PecaOrdemDeServico();
        item1.setId(id);

        PecaOrdemDeServico item2 = new PecaOrdemDeServico();
        item2.setId(id);

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void pecasComIdsDiferentesNaoDevemSerIguais() {
        PecaOrdemDeServico item1 = new PecaOrdemDeServico();
        item1.setId(UUID.randomUUID());

        PecaOrdemDeServico item2 = new PecaOrdemDeServico();
        item2.setId(UUID.randomUUID());

        assertNotEquals(item1, item2);
    }

    @Test
    void pecaComIdNuloNaoDeveSerIgual() {
        PecaOrdemDeServico item1 = new PecaOrdemDeServico();
        item1.setId(null);

        PecaOrdemDeServico item2 = new PecaOrdemDeServico();
        item2.setId(UUID.randomUUID());

        assertNotEquals(item1, item2);
    }

    @Test
    void pecaNaoDeveSerIgualANull() {
        PecaOrdemDeServico item = new PecaOrdemDeServico();
        item.setId(UUID.randomUUID());

        assertNotEquals(item, null);
    }

    @Test
    void pecaNaoDeveSerIgualAOutroTipo() {
        PecaOrdemDeServico item = new PecaOrdemDeServico();
        item.setId(UUID.randomUUID());

        assertNotEquals(item, "peca");
    }

    @Test
    void deveCriarPecaOrdemDeServicoComValoresBasicos() {
        PecaOrdemDeServico item = new PecaOrdemDeServico();

        UUID id = UUID.randomUUID();
        item.setId(id);
        item.setQuantidade(2);

        assertEquals(id, item.getId());
        assertEquals(2, item.getQuantidade());
    }
}