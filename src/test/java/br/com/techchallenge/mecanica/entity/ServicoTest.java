package br.com.techchallenge.mecanica.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class ServicoTest {

    @Test
    void servicosComMesmoIdDevemSerIguais() {
        UUID id = UUID.randomUUID();

        Servico s1 = new Servico();
        s1.setId(id);

        Servico s2 = new Servico();
        s2.setId(id);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void servicosComIdsDiferentesNaoDevemSerIguais() {
        Servico s1 = new Servico();
        s1.setId(UUID.randomUUID());

        Servico s2 = new Servico();
        s2.setId(UUID.randomUUID());

        assertNotEquals(s1, s2);
    }

    @Test
    void servicoNaoDeveSerIgualANullOuOutroTipo() {
        Servico servico = new Servico();
        servico.setId(UUID.randomUUID());

        assertNotEquals(servico, null);
        assertNotEquals(servico, 123);
    }
}
