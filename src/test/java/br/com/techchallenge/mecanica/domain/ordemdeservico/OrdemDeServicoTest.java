package br.com.techchallenge.mecanica.domain.ordemdeservico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import br.com.techchallenge.mecanica.domain.enums.StatusOrdemDeServicoEnum;
import br.com.techchallenge.mecanica.domain.enums.StatusServicoEnum;
import br.com.techchallenge.mecanica.domain.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.domain.peca.Peca;
import br.com.techchallenge.mecanica.domain.pecaordemdeservico.PecaOrdemDeServico;
import br.com.techchallenge.mecanica.domain.servico.Servico;
import br.com.techchallenge.mecanica.domain.servicoordemdeservico.ServicoOrdemDeServico;

class OrdemDeServicoTest {

    @Test
    void devePercorrerFluxoValidoDaOrdem() {
        var ordem = novaOrdem();
        var inicio = LocalDateTime.of(2026, 7, 6, 9, 0);
        var fim = inicio.plusHours(2);

        ordem.iniciarDiagnostico();
        ordem.aguardarAprovacao();
        ordem.aprovarOrcamento(inicio);
        ordem.finalizar(fim);
        ordem.entregar();

        assertEquals(StatusOrdemDeServicoEnum.ENTREGUE, ordem.getStatus());
        assertEquals(inicio, ordem.getDtInicioOs());
        assertEquals(fim, ordem.getDtFimOs());
    }

    @Test
    void deveImpedirTransicaoForaDeOrdem() {
        var ordem = novaOrdem();

        assertThrows(RegraNegocioException.class, ordem::entregar);
        assertEquals(StatusOrdemDeServicoEnum.RECEBIDA, ordem.getStatus());
    }

    @Test
    void deveSomarValoresContratadosSemConsultarPrecoAtualDoCatalogo() {
        var ordem = novaOrdem();
        var servico = new Servico("Alinhamento", new BigDecimal("120.00"));
        var peca = new Peca("Filtro", "Marca", new BigDecimal("25.00"));

        ordem.adicionarServico(new ServicoOrdemDeServico(
                UUID.randomUUID(), servico, new BigDecimal("100.00")));
        ordem.adicionarPeca(new PecaOrdemDeServico(
                UUID.randomUUID(), peca, 2, new BigDecimal("20.00")));

        assertEquals(new BigDecimal("140.00"), ordem.getValorTotalOs());
    }

    @Test
    void deveSincronizarCicloDeVidaDosServicosComAOrdem() {
        var ordem = novaOrdem();
        var servicoItem = new ServicoOrdemDeServico(UUID.randomUUID(),
                new Servico("Diagnóstico", BigDecimal.TEN), BigDecimal.TEN);
        ordem.adicionarServico(servicoItem);
        var inicio = LocalDateTime.of(2026, 7, 6, 10, 0);
        var fim = inicio.plusHours(1);

        ordem.iniciarDiagnostico();
        ordem.aguardarAprovacao();
        ordem.aprovarOrcamento(inicio);
        assertEquals(StatusServicoEnum.EM_EXECUCAO, servicoItem.getStatus());
        ordem.finalizar(fim);

        assertEquals(StatusServicoEnum.FINALIZADO, servicoItem.getStatus());
        assertEquals(inicio, servicoItem.getDtInicio());
        assertEquals(fim, servicoItem.getDtFim());
    }

    private OrdemDeServico novaOrdem() {
        return new OrdemDeServico(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    }
}
