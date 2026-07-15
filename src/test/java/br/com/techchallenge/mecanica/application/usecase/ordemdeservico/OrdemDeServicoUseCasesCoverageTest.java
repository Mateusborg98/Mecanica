package br.com.techchallenge.mecanica.application.usecase.ordemdeservico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.techchallenge.mecanica.application.dto.ordemdeservico.AdicionarItensOrdemDeServicoInput;
import br.com.techchallenge.mecanica.application.dto.ordemdeservico.AdicionarPecaOrdemDeServicoInput;
import br.com.techchallenge.mecanica.application.dto.ordemdeservico.AdicionarServicoOrdemDeServicoInput;
import br.com.techchallenge.mecanica.application.dto.ordemdeservico.CriarOrdemDeServicoInput;
import br.com.techchallenge.mecanica.application.dto.ordemdeservico.TempoMedioServicoOutput;
import br.com.techchallenge.mecanica.application.gateway.ClienteGateway;
import br.com.techchallenge.mecanica.application.gateway.EstoqueGateway;
import br.com.techchallenge.mecanica.application.gateway.NotificacaoGateway;
import br.com.techchallenge.mecanica.application.gateway.OperadorGateway;
import br.com.techchallenge.mecanica.application.gateway.OrdemDeServicoGateway;
import br.com.techchallenge.mecanica.application.gateway.PecaGateway;
import br.com.techchallenge.mecanica.application.gateway.ServicoGateway;
import br.com.techchallenge.mecanica.application.gateway.VeiculoGateway;
import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import br.com.techchallenge.mecanica.domain.enums.StatusOrdemDeServicoEnum;
import br.com.techchallenge.mecanica.domain.estoque.Estoque;
import br.com.techchallenge.mecanica.domain.exception.ClienteNaoEncontradoException;
import br.com.techchallenge.mecanica.domain.exception.EstoqueNaoEncontradoException;
import br.com.techchallenge.mecanica.domain.exception.OperadorNaoEncontradoException;
import br.com.techchallenge.mecanica.domain.exception.OrdemDeServicoNaoEncontradaException;
import br.com.techchallenge.mecanica.domain.exception.PecaNaoEncontradaException;
import br.com.techchallenge.mecanica.domain.exception.ServicoNaoEncontradoException;
import br.com.techchallenge.mecanica.domain.exception.VeiculoNaoEncontradoException;
import br.com.techchallenge.mecanica.domain.operador.Operador;
import br.com.techchallenge.mecanica.domain.ordemdeservico.OrdemDeServico;
import br.com.techchallenge.mecanica.domain.peca.Peca;
import br.com.techchallenge.mecanica.domain.servico.Servico;
import br.com.techchallenge.mecanica.domain.veiculo.Veiculo;

class OrdemDeServicoUseCasesCoverageTest {

    private OrdemDeServicoGateway ordemGateway;
    private PecaGateway pecaGateway;
    private ServicoGateway servicoGateway;
    private EstoqueGateway estoqueGateway;

    @BeforeEach
    void preparar() {
        ordemGateway = mock(OrdemDeServicoGateway.class);
        pecaGateway = mock(PecaGateway.class);
        servicoGateway = mock(ServicoGateway.class);
        estoqueGateway = mock(EstoqueGateway.class);
    }

    @Test
    void deveAdicionarServicoNaOrdem() {
        var ordemId = UUID.randomUUID();
        var servicoId = UUID.randomUUID();
        var ordem = novaOrdem();
        var servico = new Servico(servicoId, "Troca", BigDecimal.TEN, true, null);
        when(ordemGateway.buscarPorId(ordemId)).thenReturn(Optional.of(ordem));
        when(servicoGateway.buscarPorId(servicoId)).thenReturn(Optional.of(servico));
        when(ordemGateway.salvar(ordem)).thenReturn(ordem);

        var resultado = new AdicionarServicoNaOrdemDeServicoUseCase(ordemGateway, servicoGateway)
                .executar(new AdicionarServicoOrdemDeServicoInput(ordemId, servicoId));

        assertEquals(1, resultado.getServicos().size());
        assertEquals(BigDecimal.TEN, resultado.getValorTotalOs());
    }

    @Test
    void adicionarServicoDeveValidarOrdemEServico() {
        var ordemId = UUID.randomUUID();
        var servicoId = UUID.randomUUID();
        var useCase = new AdicionarServicoNaOrdemDeServicoUseCase(ordemGateway, servicoGateway);
        when(ordemGateway.buscarPorId(ordemId)).thenReturn(Optional.empty());
        assertThrows(OrdemDeServicoNaoEncontradaException.class,
                () -> useCase.executar(new AdicionarServicoOrdemDeServicoInput(ordemId, servicoId)));

        when(ordemGateway.buscarPorId(ordemId)).thenReturn(Optional.of(novaOrdem()));
        when(servicoGateway.buscarPorId(servicoId)).thenReturn(Optional.empty());
        assertThrows(ServicoNaoEncontradoException.class,
                () -> useCase.executar(new AdicionarServicoOrdemDeServicoInput(ordemId, servicoId)));
    }

    @Test
    void deveAdicionarPecaEBaixarEstoque() {
        var ordemId = UUID.randomUUID();
        var pecaId = UUID.randomUUID();
        var itemId = UUID.randomUUID();
        var ordem = novaOrdem();
        var peca = new Peca(pecaId, "Filtro", "Marca", new BigDecimal("20"));
        var estoque = new Estoque(UUID.randomUUID(), pecaId, 5);
        when(ordemGateway.buscarPorId(ordemId)).thenReturn(Optional.of(ordem));
        when(pecaGateway.buscarPorId(pecaId)).thenReturn(Optional.of(peca));
        when(estoqueGateway.buscarEstoquePorPecaId(pecaId)).thenReturn(Optional.of(estoque));
        when(ordemGateway.salvar(ordem)).thenReturn(ordem);

        var resultado = new AdicionarPecaNaOrdemDeServicoUseCase(
                ordemGateway, pecaGateway, estoqueGateway)
                .executar(new AdicionarPecaOrdemDeServicoInput(itemId, ordemId, pecaId, 2));

        assertEquals(1, resultado.getPecas().size());
        assertEquals(3, estoque.getQuantidade());
        assertEquals(new BigDecimal("40"), resultado.getValorTotalOs());
        verify(estoqueGateway).salvar(estoque);
    }

    @Test
    void adicionarPecaDeveValidarTodasAsDependencias() {
        var ordemId = UUID.randomUUID();
        var pecaId = UUID.randomUUID();
        var input = new AdicionarPecaOrdemDeServicoInput(UUID.randomUUID(), ordemId, pecaId, 1);
        var useCase = new AdicionarPecaNaOrdemDeServicoUseCase(
                ordemGateway, pecaGateway, estoqueGateway);

        assertThrows(OrdemDeServicoNaoEncontradaException.class, () -> useCase.executar(input));

        when(ordemGateway.buscarPorId(ordemId)).thenReturn(Optional.of(novaOrdem()));
        assertThrows(PecaNaoEncontradaException.class, () -> useCase.executar(input));

        when(pecaGateway.buscarPorId(pecaId)).thenReturn(Optional.of(
                new Peca(pecaId, "Filtro", "Marca", BigDecimal.TEN)));
        assertThrows(EstoqueNaoEncontradoException.class, () -> useCase.executar(input));
    }

    @Test
    void deveAdicionarColecaoDeItensEAceitarListasNulas() {
        var ordemId = UUID.randomUUID();
        var ordem = novaOrdem();
        var adicionarServico = mock(AdicionarServicoNaOrdemDeServicoUseCase.class);
        var adicionarPeca = mock(AdicionarPecaNaOrdemDeServicoUseCase.class);
        when(ordemGateway.buscarPorId(ordemId)).thenReturn(Optional.of(ordem));
        when(adicionarServico.executar(any())).thenReturn(ordem);
        when(adicionarPeca.executar(any())).thenReturn(ordem);
        var useCase = new AdicionarItensNaOrdemDeServicoUseCase(
                ordemGateway, adicionarServico, adicionarPeca);

        var resultado = useCase.executar(new AdicionarItensOrdemDeServicoInput(
                ordemId,
                List.of(UUID.randomUUID()),
                List.of(new AdicionarItensOrdemDeServicoInput.PecaInput(UUID.randomUUID(), 2))));
        assertEquals(ordem, resultado);
        verify(adicionarServico).executar(any());
        verify(adicionarPeca).executar(any());

        assertEquals(ordem, useCase.executar(
                new AdicionarItensOrdemDeServicoInput(ordemId, null, null)));

        when(ordemGateway.buscarPorId(UUID.randomUUID())).thenReturn(Optional.empty());
        assertThrows(OrdemDeServicoNaoEncontradaException.class,
                () -> useCase.executar(new AdicionarItensOrdemDeServicoInput(
                        UUID.randomUUID(), null, null)));
    }

    @Test
    void casosDeUsoDeStatusDevemSalvarENotificar() {
        var id = UUID.randomUUID();
        var ordem = mock(OrdemDeServico.class);
        var notificacao = mock(NotificarAlteracaoStatusOrdemUseCase.class);
        when(ordemGateway.buscarPorId(id)).thenReturn(Optional.of(ordem));
        when(ordemGateway.salvar(ordem)).thenReturn(ordem);

        assertEquals(ordem, new IniciarDiagnosticoUseCase(ordemGateway, notificacao).executar(id));
        assertEquals(ordem, new AguardarAprovacaoUseCase(ordemGateway, notificacao).executar(id));
        assertEquals(ordem, new AprovarOrcamentoUseCase(ordemGateway, notificacao).executar(id));
        assertEquals(ordem, new NegarOrcamentoUseCase(ordemGateway, notificacao).executar(id));
        assertEquals(ordem, new FinalizarOrdemDeServicoUseCase(ordemGateway, notificacao).executar(id));
        assertEquals(ordem, new EntregarOrdemDeServicoUseCase(ordemGateway, notificacao).executar(id));

        verify(ordem).iniciarDiagnostico();
        verify(ordem).aguardarAprovacao();
        verify(ordem).aprovarOrcamento(any());
        verify(ordem).negarOrcamento();
        verify(ordem).finalizar(any());
        verify(ordem).entregar();
        verify(notificacao, times(6)).executar(ordem);
        verify(ordemGateway, times(6)).salvar(ordem);
    }

    @Test
    void casosDeUsoDeStatusDevemFalharQuandoOrdemNaoExiste() {
        var id = UUID.randomUUID();
        var notificacao = mock(NotificarAlteracaoStatusOrdemUseCase.class);
        when(ordemGateway.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(OrdemDeServicoNaoEncontradaException.class,
                () -> new IniciarDiagnosticoUseCase(ordemGateway, notificacao).executar(id));
        assertThrows(OrdemDeServicoNaoEncontradaException.class,
                () -> new AguardarAprovacaoUseCase(ordemGateway, notificacao).executar(id));
        assertThrows(OrdemDeServicoNaoEncontradaException.class,
                () -> new AprovarOrcamentoUseCase(ordemGateway, notificacao).executar(id));
        assertThrows(OrdemDeServicoNaoEncontradaException.class,
                () -> new NegarOrcamentoUseCase(ordemGateway, notificacao).executar(id));
        assertThrows(OrdemDeServicoNaoEncontradaException.class,
                () -> new FinalizarOrdemDeServicoUseCase(ordemGateway, notificacao).executar(id));
        assertThrows(OrdemDeServicoNaoEncontradaException.class,
                () -> new EntregarOrdemDeServicoUseCase(ordemGateway, notificacao).executar(id));
        verify(notificacao, never()).executar(any());
    }

    @Test
    void consultasDeOrdemDevemDelegarAoGatewayECobrirAusencia() {
        var id = UUID.randomUUID();
        var ordem = novaOrdem();
        var media = new TempoMedioServicoOutput(UUID.randomUUID(), 30);
        when(ordemGateway.buscarPorId(id)).thenReturn(Optional.of(ordem));
        when(ordemGateway.listar()).thenReturn(List.of(ordem));
        when(ordemGateway.calcularTempoMedioServicos()).thenReturn(List.of(media));

        assertEquals(ordem, new BuscarOrdemDeServicoPorIdUseCase(ordemGateway).executar(id));
        assertEquals(List.of(ordem), new ListarOrdensDeServicoUseCase(ordemGateway).executar());
        assertEquals(List.of(media), new CalcularTempoMedioServicosUseCase(ordemGateway).executar());

        assertThrows(OrdemDeServicoNaoEncontradaException.class,
                () -> new BuscarOrdemDeServicoPorIdUseCase(ordemGateway).executar(UUID.randomUUID()));
    }

    @Test
    void notificacaoDeveIgnorarObjetosIncompletosEEnviarMensagemValida() {
        var gateway = mock(NotificacaoGateway.class);
        var useCase = new NotificarAlteracaoStatusOrdemUseCase(gateway);
        useCase.executar(null);
        useCase.executar(novaOrdem());

        var semStatus = mock(OrdemDeServico.class);
        when(semStatus.getId()).thenReturn(UUID.randomUUID());
        when(semStatus.getStatus()).thenReturn(null);
        useCase.executar(semStatus);
        verify(gateway, never()).notificarAlteracaoStatusOrdem(any(), any(), any());

        var id = UUID.randomUUID();
        var valida = mock(OrdemDeServico.class);
        when(valida.getId()).thenReturn(id);
        when(valida.getStatus()).thenReturn(StatusOrdemDeServicoEnum.EM_EXECUCAO);
        useCase.executar(valida);

        verify(gateway).notificarAlteracaoStatusOrdem(
                eq(id), eq("EM_EXECUCAO"), contains(id.toString()));
    }

    @Test
    void criarOrdemDeveAceitarListasNulas() {
        var clienteGateway = mock(ClienteGateway.class);
        var veiculoGateway = mock(VeiculoGateway.class);
        var operadorGateway = mock(OperadorGateway.class);
        var clienteId = UUID.randomUUID();
        var veiculoId = UUID.randomUUID();
        var operadorId = UUID.randomUUID();
        when(clienteGateway.buscarPorId(clienteId)).thenReturn(Optional.of(mock(Cliente.class)));
        when(veiculoGateway.buscarPorId(veiculoId)).thenReturn(Optional.of(mock(Veiculo.class)));
        when(operadorGateway.buscarPorId(operadorId)).thenReturn(Optional.of(mock(Operador.class)));
        when(ordemGateway.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = new CriarOrdemDeServicoUseCase(
                ordemGateway, clienteGateway, veiculoGateway, operadorGateway,
                servicoGateway, pecaGateway, estoqueGateway)
                .executar(new CriarOrdemDeServicoInput(
                        clienteId, veiculoId, operadorId, null, null));

        assertEquals(StatusOrdemDeServicoEnum.RECEBIDA, resultado.getStatus());
        assertEquals(BigDecimal.ZERO, resultado.getValorTotalOs());
    }

    @Test
    void criarOrdemDeveValidarCadastrosEItens() {
        var clienteGateway = mock(ClienteGateway.class);
        var veiculoGateway = mock(VeiculoGateway.class);
        var operadorGateway = mock(OperadorGateway.class);
        var clienteId = UUID.randomUUID();
        var veiculoId = UUID.randomUUID();
        var operadorId = UUID.randomUUID();
        var servicoId = UUID.randomUUID();
        var pecaId = UUID.randomUUID();
        var useCase = new CriarOrdemDeServicoUseCase(
                ordemGateway, clienteGateway, veiculoGateway, operadorGateway,
                servicoGateway, pecaGateway, estoqueGateway);

        var basico = new CriarOrdemDeServicoInput(clienteId, veiculoId, operadorId);
        assertThrows(ClienteNaoEncontradoException.class, () -> useCase.executar(basico));

        when(clienteGateway.buscarPorId(clienteId)).thenReturn(Optional.of(mock(Cliente.class)));
        assertThrows(VeiculoNaoEncontradoException.class, () -> useCase.executar(basico));

        when(veiculoGateway.buscarPorId(veiculoId)).thenReturn(Optional.of(mock(Veiculo.class)));
        assertThrows(OperadorNaoEncontradoException.class, () -> useCase.executar(basico));

        when(operadorGateway.buscarPorId(operadorId)).thenReturn(Optional.of(mock(Operador.class)));
        var comServico = new CriarOrdemDeServicoInput(
                clienteId, veiculoId, operadorId, List.of(servicoId), List.of());
        assertThrows(ServicoNaoEncontradoException.class, () -> useCase.executar(comServico));

        var comPeca = new CriarOrdemDeServicoInput(
                clienteId, veiculoId, operadorId, List.of(),
                List.of(new CriarOrdemDeServicoInput.PecaInput(pecaId, 1)));
        assertThrows(PecaNaoEncontradaException.class, () -> useCase.executar(comPeca));

        when(pecaGateway.buscarPorId(pecaId)).thenReturn(Optional.of(
                new Peca(pecaId, "Filtro", "Marca", BigDecimal.TEN)));
        assertThrows(EstoqueNaoEncontradoException.class, () -> useCase.executar(comPeca));
    }

    private OrdemDeServico novaOrdem() {
        return new OrdemDeServico(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    }
}
