package br.com.techchallenge.mecanica.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class ItemOrdemDeServicoTest {

    // =========================
    // equals / hashCode
    // =========================

    @Test
    void itensComMesmoIdDevemSerIguais() {
        UUID id = UUID.randomUUID();

        ItemOrdemDeServico item1 = new ItemOrdemDeServico();
        item1.setId(id);

        ItemOrdemDeServico item2 = new ItemOrdemDeServico();
        item2.setId(id);

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void itensComIdsDiferentesNaoDevemSerIguais() {
        ItemOrdemDeServico item1 = new ItemOrdemDeServico();
        item1.setId(UUID.randomUUID());

        ItemOrdemDeServico item2 = new ItemOrdemDeServico();
        item2.setId(UUID.randomUUID());

        assertNotEquals(item1, item2);
    }

    @Test
    void itemComIdNuloNaoDeveSerIgual() {
        ItemOrdemDeServico item1 = new ItemOrdemDeServico();
        item1.setId(null);

        ItemOrdemDeServico item2 = new ItemOrdemDeServico();
        item2.setId(UUID.randomUUID());

        assertNotEquals(item1, item2);
    }

    @Test
    void itemNaoDeveSerIgualANull() {
        ItemOrdemDeServico item = new ItemOrdemDeServico();
        item.setId(UUID.randomUUID());

        assertNotEquals(item, null);
    }

    @Test
    void itemNaoDeveSerIgualAOutroTipo() {
        ItemOrdemDeServico item = new ItemOrdemDeServico();
        item.setId(UUID.randomUUID());

        assertNotEquals(item, "item");
    }

    // =========================
    // validação estrutural simples
    // =========================

    @Test
    void deveCriarItemComValoresBasicos() {
        ItemOrdemDeServico item = new ItemOrdemDeServico();

        item.setId(UUID.randomUUID());
        item.setQuantidade(2);
        item.setValorUnitario(new BigDecimal("100"));

        assertEquals(2, item.getQuantidade());
        assertEquals(new BigDecimal("100"), item.getValorUnitario());
    }
}