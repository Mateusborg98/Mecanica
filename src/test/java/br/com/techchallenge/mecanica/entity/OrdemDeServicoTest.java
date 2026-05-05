package br.com.techchallenge.mecanica.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class OrdemDeServicoTest {

    @Test
    void deveIniciarComStatusRecebida() {
        OrdemDeServico os = new OrdemDeServico();
        os.setStatus(StatusOrdemDeServicoEnum.RECEBIDA);

        assertEquals(StatusOrdemDeServicoEnum.RECEBIDA, os.getStatus());
    }

    @Test
    void deveAdicionarServicoComSucesso() {
        OrdemDeServico os = new OrdemDeServico();

        Servico servico = new Servico();
        servico.setDescricao("Troca de óleo");
        servico.setPreco(new BigDecimal("150.00"));

        os.adicionarServico(servico);

        assertEquals(1, os.getServicos().size());
    }

    @Test
    void naoDeveAdicionarServicoNulo() {
        OrdemDeServico os = new OrdemDeServico();

        assertThrows(IllegalArgumentException.class, () -> os.adicionarServico(null));
    }

    @Test
    void deveAdicionarPecaComQuantidadeValida() {
        OrdemDeServico os = new OrdemDeServico();

        Peca peca = new Peca();
        peca.setNome("Filtro de óleo");
        peca.setPreco(new BigDecimal("50.00"));

        os.adicionarPeca(peca, 2);

        assertEquals(1, os.getItens().size());
    }

    @Test
    void naoDeveAdicionarPecaNula() {
        OrdemDeServico os = new OrdemDeServico();

        assertThrows(IllegalArgumentException.class,
                () -> os.adicionarPeca(null, 1));
    }

    @Test
    void naoDeveAdicionarPecaComQuantidadeInvalida() {
        OrdemDeServico os = new OrdemDeServico();

        Peca peca = new Peca();
        peca.setPreco(new BigDecimal("30.00"));

        assertThrows(IllegalArgumentException.class,
                () -> os.adicionarPeca(peca, 0));
    }

    @Test
    void deveCalcularValorTotal() {
        OrdemDeServico os = new OrdemDeServico();
        Servico servico = new Servico(UUID.randomUUID(), "Troca de óleo", new BigDecimal("20"), os,
                StatusServicoEnum.AGUARDANDO, LocalDateTime.now());
        List<Servico> servicos = new ArrayList<>();
        servicos.add(servico);
        os.setServicos(servicos);

        Peca peca = new Peca(UUID.randomUUID(), "Óleo", "Petronas", new BigDecimal("40"));
        ItemOrdemDeServico itemOrdemDeServico = new ItemOrdemDeServico(UUID.randomUUID(), os, peca, 4,
                new BigDecimal("40"));
        List<ItemOrdemDeServico> itemOrdemDeServicos = new ArrayList<>();
        itemOrdemDeServicos.add(itemOrdemDeServico);

        assertEquals(new BigDecimal("180"), os.calcularValorTotal(servicos, itemOrdemDeServicos));
    }

    @Test
    void deveRetornarTrueQuandoMesmaReferencia() {
        OrdemDeServico os = new OrdemDeServico();
        assertEquals(os, os);
    }

    @Test
    void deveRetornarFalseQuandoObjetoForNulo() {
        OrdemDeServico os = new OrdemDeServico();
        assertNotEquals(os, null);
    }

    @Test
    void deveRetornarFalseQuandoObjetoForOutroTipo() {
        OrdemDeServico os = new OrdemDeServico();
        assertNotEquals(os, "ordem");
    }

    @Test
    void deveRetornarFalseQuandoIdForNulo() {
        OrdemDeServico os1 = new OrdemDeServico();
        OrdemDeServico os2 = new OrdemDeServico();

        assertNotEquals(os1, os2);
    }

    @Test
    void deveRetornarTrueQuandoIdsForemIguais() {
        UUID id = UUID.randomUUID();

        OrdemDeServico os1 = new OrdemDeServico();
        os1.setId(id);

        OrdemDeServico os2 = new OrdemDeServico();
        os2.setId(id);

        assertEquals(os1, os2);
        assertEquals(os1.hashCode(), os2.hashCode());
    }

    @Test
    void deveRetornarFalseQuandoIdsForemDiferentes() {
        OrdemDeServico os1 = new OrdemDeServico();
        os1.setId(UUID.randomUUID());

        OrdemDeServico os2 = new OrdemDeServico();
        os2.setId(UUID.randomUUID());

        assertNotEquals(os1, os2);
    }

}
