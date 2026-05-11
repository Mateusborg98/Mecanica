package br.com.techchallenge.mecanica.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import br.com.techchallenge.mecanica.exception.RegraNegocioException;

class EstoqueTest {

    @Test
    void deveRegistrarEntradaComSucesso() {
        Estoque estoque = new Estoque();
        estoque.setQuantidade(10);

        estoque.registrarEntrada(5);

        assertEquals(15, estoque.getQuantidade());
    }

    @Test
    void naoDeveRegistrarEntradaComQuantidadeZero() {
        Estoque estoque = new Estoque();
        estoque.setQuantidade(10);

        assertThrows(RegraNegocioException.class,
                () -> estoque.registrarEntrada(0));
    }

    @Test
    void naoDeveRegistrarEntradaComQuantidadeNegativa() {
        Estoque estoque = new Estoque();
        estoque.setQuantidade(10);

        assertThrows(RegraNegocioException.class,
                () -> estoque.registrarEntrada(-3));
    }

    @Test
    void deveRegistrarSaidaComSucesso() {
        Estoque estoque = new Estoque();
        estoque.setQuantidade(10);

        estoque.registrarSaida(4);

        assertEquals(6, estoque.getQuantidade());
    }

    @Test
    void naoDeveRegistrarSaidaComQuantidadeZero() {
        Estoque estoque = new Estoque();
        estoque.setQuantidade(10);

        assertThrows(RegraNegocioException.class,
                () -> estoque.registrarSaida(0));
    }

    @Test
    void naoDeveRegistrarSaidaComQuantidadeNegativa() {
        Estoque estoque = new Estoque();
        estoque.setQuantidade(10);

        assertThrows(RegraNegocioException.class,
                () -> estoque.registrarSaida(-2));
    }

    @Test
    void naoDeveRegistrarSaidaQuandoEstoqueInsuficiente() {
        Estoque estoque = new Estoque();
        estoque.setQuantidade(5);

        assertThrows(RegraNegocioException.class,
                () -> estoque.registrarSaida(10));
    }

    @Test
    void estoquesComMesmoIdDevemSerIguais() {
        UUID id = UUID.randomUUID();

        Estoque estoque1 = new Estoque();
        estoque1.setId(id);

        Estoque estoque2 = new Estoque();
        estoque2.setId(id);

        assertEquals(estoque1, estoque2);
        assertEquals(estoque1.hashCode(), estoque2.hashCode());
    }

    @Test
    void estoquesComIdsDiferentesNaoDevemSerIguais() {
        Estoque estoque1 = new Estoque();
        estoque1.setId(UUID.randomUUID());

        Estoque estoque2 = new Estoque();
        estoque2.setId(UUID.randomUUID());

        assertNotEquals(estoque1, estoque2);
    }

    @Test
    void estoqueNaoDeveSerIgualANull() {
        Estoque estoque = new Estoque();
        estoque.setId(UUID.randomUUID());

        assertNotEquals(null, estoque);
    }

    @Test
    void estoqueNaoDeveSerIgualAOutroTipo() {
        Estoque estoque = new Estoque();
        estoque.setId(UUID.randomUUID());

        assertNotEquals(estoque, "estoque");
    }
}