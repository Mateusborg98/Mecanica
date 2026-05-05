package br.com.techchallenge.mecanica.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class ClienteTest {

    @Test
    void clientesComMesmoIdDevemSerIguais() {
        UUID id = UUID.randomUUID();

        Cliente c1 = new Cliente();
        c1.setId(id);

        Cliente c2 = new Cliente();
        c2.setId(id);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void clientesComIdsDiferentesNaoDevemSerIguais() {
        Cliente c1 = new Cliente();
        c1.setId(UUID.randomUUID());

        Cliente c2 = new Cliente();
        c2.setId(UUID.randomUUID());

        assertNotEquals(c1, c2);
    }

    @Test
    void clienteNaoDeveSerIgualANullOuOutroTipo() {
        Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID());

        assertNotEquals(cliente, null);
        assertNotEquals(cliente, "cliente");
    }
}