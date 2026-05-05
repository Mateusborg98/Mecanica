package br.com.techchallenge.mecanica.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class VeiculoTest {

    @Test
    void veiculosComMesmoIdDevemSerIguais() {
        UUID id = UUID.randomUUID();

        Veiculo v1 = new Veiculo();
        v1.setId(id);

        Veiculo v2 = new Veiculo();
        v2.setId(id);

        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());
    }

    @Test
    void veiculosComIdsDiferentesNaoDevemSerIguais() {
        Veiculo v1 = new Veiculo();
        v1.setId(UUID.randomUUID());

        Veiculo v2 = new Veiculo();
        v2.setId(UUID.randomUUID());

        assertNotEquals(v1, v2);
    }

    @Test
    void veiculoNaoDeveSerIgualANullOuOutroTipo() {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(UUID.randomUUID());

        assertNotEquals(veiculo, null);
        assertNotEquals(veiculo, "veiculo");
    }
}
