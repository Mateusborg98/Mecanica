package br.com.techchallenge.mecanica.domain.estoque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import br.com.techchallenge.mecanica.domain.exception.QuantidadeEstoqueException;

class EstoqueTest {

    @Test
    void deveRegistrarEntrada() {
        var estoque = new Estoque(UUID.randomUUID(), 5);
        estoque.registrarEntrada(3);
        assertEquals(8, estoque.getQuantidade());
    }

    @Test
    void deveRegistrarSaida() {
        var estoque = new Estoque(UUID.randomUUID(), 5);
        estoque.registrarSaida(2);
        assertEquals(3, estoque.getQuantidade());
    }

    @Test
    void deveImpedirSaidaMaiorQueSaldo() {
        var estoque = new Estoque(UUID.randomUUID(), 2);
        assertThrows(QuantidadeEstoqueException.class, () -> estoque.registrarSaida(3));
    }

    @Test
    void deveRejeitarMovimentacoesNaoPositivas() {
        var estoque = new Estoque(UUID.randomUUID(), 2);
        assertThrows(QuantidadeEstoqueException.class, () -> estoque.registrarEntrada(0));
        assertThrows(QuantidadeEstoqueException.class, () -> estoque.registrarSaida(-1));
    }

    @Test
    void devePreservarIdAoReconstruir() {
        var id = UUID.randomUUID();
        var estoque = new Estoque(id, UUID.randomUUID(), 2);
        assertEquals(id, estoque.getId());
    }

    @Test
    void deveExigirPecaEQuantidadeValida() {
        assertThrows(QuantidadeEstoqueException.class, () -> new Estoque(null, 1));
        assertThrows(QuantidadeEstoqueException.class, () -> new Estoque(UUID.randomUUID(), -1));
    }
}
