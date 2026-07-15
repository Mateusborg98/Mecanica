package br.com.techchallenge.mecanica.infrastructure.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class JpaEntityContractTest {

    @Test
    void clienteDeveCompararPorId() {
        var id = UUID.randomUUID();
        validarContrato(ClienteJpaEntity.builder().id(id).build(),
                ClienteJpaEntity.builder().id(id).build(),
                ClienteJpaEntity.builder().build());
    }

    @Test
    void estoqueDeveCompararPorId() {
        var id = UUID.randomUUID();
        validarContrato(EstoqueJpaEntity.builder().id(id).build(),
                EstoqueJpaEntity.builder().id(id).build(),
                EstoqueJpaEntity.builder().build());
    }

    @Test
    void operadorDeveCompararPorId() {
        var id = UUID.randomUUID();
        validarContrato(OperadorJpaEntity.builder().id(id).build(),
                OperadorJpaEntity.builder().id(id).build(),
                OperadorJpaEntity.builder().build());
    }

    @Test
    void ordemDeveCompararPorId() {
        var id = UUID.randomUUID();
        validarContrato(OrdemDeServicoJpaEntity.builder().id(id).build(),
                OrdemDeServicoJpaEntity.builder().id(id).build(),
                OrdemDeServicoJpaEntity.builder().build());
    }

    @Test
    void pecaDeveCompararPorId() {
        var id = UUID.randomUUID();
        validarContrato(PecaJpaEntity.builder().id(id).build(),
                PecaJpaEntity.builder().id(id).build(),
                PecaJpaEntity.builder().build());
    }

    @Test
    void pecaDaOrdemDeveCompararPorId() {
        var id = UUID.randomUUID();
        validarContrato(PecaOrdemDeServicoJpaEntity.builder().id(id).build(),
                PecaOrdemDeServicoJpaEntity.builder().id(id).build(),
                PecaOrdemDeServicoJpaEntity.builder().build());
    }

    @Test
    void servicoDeveCompararPorId() {
        var id = UUID.randomUUID();
        validarContrato(ServicoJpaEntity.builder().id(id).build(),
                ServicoJpaEntity.builder().id(id).build(),
                ServicoJpaEntity.builder().build());
    }

    @Test
    void servicoDaOrdemDeveCompararPorId() {
        var id = UUID.randomUUID();
        validarContrato(ServicoOrdemDeServicoJpaEntity.builder().id(id).build(),
                ServicoOrdemDeServicoJpaEntity.builder().id(id).build(),
                ServicoOrdemDeServicoJpaEntity.builder().build());
    }

    @Test
    void veiculoDeveCompararPorId() {
        var id = UUID.randomUUID();
        validarContrato(VeiculoJpaEntity.builder().id(id).build(),
                VeiculoJpaEntity.builder().id(id).build(),
                VeiculoJpaEntity.builder().build());
    }

    private void validarContrato(Object original, Object mesmoId, Object semId) {
        assertEquals(original, original);
        assertEquals(original, mesmoId);
        assertEquals(original.hashCode(), mesmoId.hashCode());
        assertNotEquals(original, semId);
        assertNotEquals(original, new Object());
    }
}
