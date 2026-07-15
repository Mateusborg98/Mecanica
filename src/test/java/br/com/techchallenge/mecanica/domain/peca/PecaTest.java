package br.com.techchallenge.mecanica.domain.peca;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import br.com.techchallenge.mecanica.domain.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.domain.pecaordemdeservico.PecaOrdemDeServico;

class PecaTest {

    @Test
    void devePreservarIdAoReconstruirPecaPersistida() {
        var id = UUID.randomUUID();

        var peca = new Peca(id, "Pastilha", "Marca", new BigDecimal("80.00"));

        assertEquals(id, peca.getId());
    }

    @Test
    void deveRejeitarValorUnitarioNegativoNaOrdem() {
        var peca = new Peca("Pastilha", "Marca", new BigDecimal("80.00"));

        assertThrows(RegraNegocioException.class, () ->
                new PecaOrdemDeServico(UUID.randomUUID(), peca, 1, new BigDecimal("-1.00")));
    }
}
