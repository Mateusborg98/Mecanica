package br.com.techchallenge.mecanica.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import br.com.techchallenge.mecanica.exception.RegraNegocioException;

class OrdemDeServicoTest {

    @Test
    void deveAdicionarServico() {

        OrdemDeServico os = new OrdemDeServico();

        Servico servico = new Servico();
        servico.setPreco(new BigDecimal("100.00"));

        ServicoOrdemDeServico sos = new ServicoOrdemDeServico();

        sos.setServico(servico);

        os.adicionarServico(sos);

        assertEquals(1, os.getServicos().size());
    }

    @Test
    void deveLancarExcecaoAoAdicionarServicoNulo() {

        OrdemDeServico os = new OrdemDeServico();

        RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                () -> os.adicionarServico(null));

        assertEquals("Serviço não pode ser nulo",
                ex.getMessage());
    }

    @Test
    void deveAdicionarPeca() {

        OrdemDeServico os = new OrdemDeServico();

        Peca peca = new Peca();
        peca.setPreco(new BigDecimal("50.00"));

        PecaOrdemDeServico pos = new PecaOrdemDeServico();

        pos.setPeca(peca);
        pos.setQuantidade(2);

        os.adicionarPeca(pos);

        assertEquals(1, os.getPecas().size());
    }

    @Test
    void deveLancarExcecaoAoAdicionarPecaNula() {

        OrdemDeServico os = new OrdemDeServico();

        RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                () -> os.adicionarPeca(null));

        assertEquals("Peça não pode ser nula",
                ex.getMessage());
    }

    @Test
    void deveCalcularValorTotal() {

        OrdemDeServico os = new OrdemDeServico();

        // Serviço
        Servico servico = new Servico();
        servico.setPreco(new BigDecimal("100.00"));

        ServicoOrdemDeServico sos = new ServicoOrdemDeServico();

        sos.setServico(servico);

        os.adicionarServico(sos);

        // Peça
        Peca peca = new Peca();
        peca.setPreco(new BigDecimal("50.00"));

        PecaOrdemDeServico pos = new PecaOrdemDeServico();

        pos.setPeca(peca);
        pos.setQuantidade(2);

        os.adicionarPeca(pos);

        BigDecimal total = os.calcularValorTotal();

        assertEquals(new BigDecimal("200.00"), total);
    }

    @Test
    void deveRetornarZeroQuandoNaoPossuirItens() {

        OrdemDeServico os = new OrdemDeServico();

        BigDecimal total = os.calcularValorTotal();

        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    void deveRetornarTrueQuandoIdsForemIguais() {

        UUID id = UUID.randomUUID();

        OrdemDeServico os1 = new OrdemDeServico();
        os1.setId(id);

        OrdemDeServico os2 = new OrdemDeServico();
        os2.setId(id);

        assertEquals(os1, os2);
    }

    @Test
    void deveRetornarFalseQuandoIdsForemDiferentes() {

        OrdemDeServico os1 = new OrdemDeServico();
        os1.setId(UUID.randomUUID());

        OrdemDeServico os2 = new OrdemDeServico();
        os2.setId(UUID.randomUUID());

        assertNotEquals(os1, os2);
    }

    @Test
    void deveRetornarFalseQuandoObjetoForNulo() {

        OrdemDeServico os = new OrdemDeServico();
        os.setId(UUID.randomUUID());

        assertNotEquals(os, null);
    }

    @Test
    void deveRetornarFalseQuandoObjetoForDeOutroTipo() {

        OrdemDeServico os = new OrdemDeServico();
        os.setId(UUID.randomUUID());

        assertNotEquals(os, "teste");
    }

    @Test
    void deveRetornarMesmoHashCodeDaClasse() {

        OrdemDeServico os = new OrdemDeServico();

        assertEquals(
                OrdemDeServico.class.hashCode(),
                os.hashCode());
    }

    @Test
    void deveRetornarTrueQuandoCompararMesmaInstancia() {

        OrdemDeServico os = new OrdemDeServico();

        assertTrue(os.equals(os));
    }
}