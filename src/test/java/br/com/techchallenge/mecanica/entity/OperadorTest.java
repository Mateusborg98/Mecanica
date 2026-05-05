package br.com.techchallenge.mecanica.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class OperadorTest {

    @Test
    void operadoresComMesmoIdDevemSerIguais() {
        UUID id = UUID.randomUUID();

        Operador o1 = new Operador();
        o1.setId(id);

        Operador o2 = new Operador();
        o2.setId(id);

        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
    }

    @Test
    void operadoresComIdsDiferentesNaoDevemSerIguais() {
        Operador o1 = new Operador();
        o1.setId(UUID.randomUUID());

        Operador o2 = new Operador();
        o2.setId(UUID.randomUUID());

        assertNotEquals(o1, o2);
    }

    @Test
    void operadorNaoDeveSerIgualANullOuOutroTipo() {
        Operador operador = new Operador();
        operador.setId(UUID.randomUUID());

        assertNotEquals(operador, null);
        assertNotEquals(operador, true);
    }
}