package br.com.techchallenge.mecanica.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class PecaTest {

    @Test
    void pecasComMesmoIdDevemSerIguais() {
        UUID id = UUID.randomUUID();

        Peca p1 = new Peca();
        p1.setId(id);

        Peca p2 = new Peca();
        p2.setId(id);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void pecasComIdsDiferentesNaoDevemSerIguais() {
        Peca p1 = new Peca();
        p1.setId(UUID.randomUUID());

        Peca p2 = new Peca();
        p2.setId(UUID.randomUUID());

        assertNotEquals(p1, p2);
    }

    @Test
    void pecaNaoDeveSerIgualANullOuOutroTipo() {
        Peca peca = new Peca();
        peca.setId(UUID.randomUUID());

        assertNotEquals(peca, null);
        assertNotEquals(peca, 10);
    }
}