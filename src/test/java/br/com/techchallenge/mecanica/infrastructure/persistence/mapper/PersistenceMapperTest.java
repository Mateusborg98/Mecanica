package br.com.techchallenge.mecanica.infrastructure.persistence.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ClienteJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.OperadorJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.VeiculoJpaEntity;
import br.com.techchallenge.mecanica.domain.ordemdeservico.OrdemDeServico;
import br.com.techchallenge.mecanica.domain.peca.Peca;
import br.com.techchallenge.mecanica.domain.pecaordemdeservico.PecaOrdemDeServico;
import br.com.techchallenge.mecanica.domain.servico.Servico;
import br.com.techchallenge.mecanica.domain.servicoordemdeservico.ServicoOrdemDeServico;

class PersistenceMapperTest {

    @Test
    void clienteDevePreservarIdentidadeEEstadoNoRoundTrip() {
        var id = UUID.randomUUID();
        var inativacao = LocalDateTime.of(2026, 7, 6, 10, 0);
        var entity = ClienteJpaEntity.builder()
                .id(id).nome("Cliente").cpfCnpj("52998224725")
                .contato("11999999999").email("cliente@email.com")
                .ativo(false).dataInativacao(inativacao).build();
        var mapper = new ClienteMapper();

        var domain = mapper.toDomain(entity);
        var novamente = mapper.toJpaEntity(domain);

        assertEquals(id, domain.getId());
        assertFalse(domain.isAtivo());
        assertEquals(inativacao, domain.getDataInativacao());
        assertEquals(id, novamente.getId());
        assertFalse(novamente.isAtivo());
    }

    @Test
    void operadorDevePreservarIdCargoEEstadoNoRoundTrip() {
        var id = UUID.randomUUID();
        var entity = OperadorJpaEntity.builder()
                .id(id).nome("Operador").matricula(10)
                .email("operador@email.com").contato("11999999999")
                .cargo("MECANICO").ativo(true).build();
        var mapper = new OperadorMapper();

        var domain = mapper.toDomain(entity);
        var novamente = mapper.toJpaEntity(domain);

        assertEquals(id, domain.getId());
        assertEquals("MECANICO", domain.getCargo());
        assertTrue(domain.isAtivo());
        assertEquals("MECANICO", novamente.getCargo());
        assertTrue(novamente.isAtivo());
    }

    @Test
    void veiculoDevePreservarIdClienteEEstadoNoRoundTrip() {
        var clienteId = UUID.randomUUID();
        var veiculoId = UUID.randomUUID();
        var cliente = ClienteJpaEntity.builder()
                .id(clienteId).nome("Cliente").cpfCnpj("52998224725")
                .contato("11999999999").email("cliente@email.com").ativo(true).build();
        var entity = VeiculoJpaEntity.builder()
                .id(veiculoId).placa("ABC1D23").marca("Honda")
                .modelo("Civic").ano(2020).clienteJpaEntity(cliente)
                .ativo(false).dataInativacao(LocalDateTime.now()).build();
        var mapper = new VeiculoMapper(new ClienteMapper());

        var domain = mapper.toDomain(entity);

        assertEquals(veiculoId, domain.getId());
        assertEquals(clienteId, domain.getClienteId());
        assertFalse(domain.isAtivo());
    }

    @Test
    void ordemDeveMapearValoresEVinculoPaiDosItens() {
        var ordem = new OrdemDeServico(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        var peca = new Peca(UUID.randomUUID(), "Filtro", "Marca", BigDecimal.TEN, true, null);
        var servico = new Servico(UUID.randomUUID(), "Troca", new BigDecimal("20.00"), true, null);
        ordem.adicionarPeca(new PecaOrdemDeServico(
                UUID.randomUUID(), peca, 2, new BigDecimal("9.00")));
        ordem.adicionarServico(new ServicoOrdemDeServico(
                UUID.randomUUID(), servico, new BigDecimal("18.00")));

        var pecaMapper = new PecaOrdemDeServicoMapper(new PecaMapper());
        var servicoMapper = new ServicoOrdemDeServicoMapper(new ServicoMapper());
        var entity = new OrdemDeServicoMapper(pecaMapper, servicoMapper).toEntity(ordem);

        assertEquals(new BigDecimal("9.00"), entity.getPecas().getFirst().getValorUnitario());
        assertEquals(new BigDecimal("18.00"), entity.getServicos().getFirst().getValorCobrado());
        assertEquals(entity, entity.getPecas().getFirst().getOrdemDeServicoJpaEntity());
        assertEquals(entity, entity.getServicos().getFirst().getOrdemDeServicoJpaEntity());
    }

    @Test
    void pecaEServicoDevemPreservarIdentidadeEEstado() {
        var inativacao = LocalDateTime.of(2026, 7, 6, 12, 0);
        var pecaEntity = br.com.techchallenge.mecanica.infrastructure.persistence.entity.PecaJpaEntity.builder()
                .id(UUID.randomUUID()).nome("Filtro").marca("Marca").preco(BigDecimal.TEN)
                .ativo(false).dataInativacao(inativacao).build();
        var servicoEntity = br.com.techchallenge.mecanica.infrastructure.persistence.entity.ServicoJpaEntity.builder()
                .id(UUID.randomUUID()).descricao("Troca").preco(BigDecimal.TEN)
                .ativo(false).dataInativacao(inativacao).build();

        var peca = new PecaMapper().toDomain(pecaEntity);
        var servico = new ServicoMapper().toDomain(servicoEntity);

        assertEquals(pecaEntity.getId(), peca.getId());
        assertFalse(peca.isAtivo());
        assertEquals(servicoEntity.getId(), servico.getId());
        assertFalse(servico.isAtivo());
    }
}
