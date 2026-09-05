package br.com.techchallenge.mecanica.application.usecase.ordemdeservico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.techchallenge.mecanica.application.dto.ordemdeservico.CriarOrdemDeServicoInput;
import br.com.techchallenge.mecanica.application.gateway.ClienteGateway;
import br.com.techchallenge.mecanica.application.gateway.EstoqueGateway;
import br.com.techchallenge.mecanica.application.gateway.OperadorGateway;
import br.com.techchallenge.mecanica.application.gateway.OrdemDeServicoGateway;
import br.com.techchallenge.mecanica.application.gateway.PecaGateway;
import br.com.techchallenge.mecanica.application.gateway.ServicoGateway;
import br.com.techchallenge.mecanica.application.gateway.VeiculoGateway;
import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import br.com.techchallenge.mecanica.domain.estoque.Estoque;
import br.com.techchallenge.mecanica.domain.exception.QuantidadeEstoqueException;
import br.com.techchallenge.mecanica.domain.operador.Operador;
import br.com.techchallenge.mecanica.domain.ordemdeservico.OrdemDeServico;
import br.com.techchallenge.mecanica.domain.peca.Peca;
import br.com.techchallenge.mecanica.domain.servico.Servico;
import br.com.techchallenge.mecanica.domain.veiculo.Veiculo;

class CriarOrdemDeServicoUseCaseTest {

    private OrdemDeServicoGateway ordemGateway;
    private ClienteGateway clienteGateway;
    private VeiculoGateway veiculoGateway;
    private OperadorGateway operadorGateway;
    private ServicoGateway servicoGateway;
    private PecaGateway pecaGateway;
    private EstoqueGateway estoqueGateway;
    private NotificarAlteracaoStatusOrdemUseCase notificacaoUseCase;
    private CriarOrdemDeServicoUseCase useCase;

    @BeforeEach
    void preparar() {
        ordemGateway = mock(OrdemDeServicoGateway.class);
        clienteGateway = mock(ClienteGateway.class);
        veiculoGateway = mock(VeiculoGateway.class);
        operadorGateway = mock(OperadorGateway.class);
        servicoGateway = mock(ServicoGateway.class);
        pecaGateway = mock(PecaGateway.class);
        estoqueGateway = mock(EstoqueGateway.class);
        notificacaoUseCase = mock(NotificarAlteracaoStatusOrdemUseCase.class);
        useCase = new CriarOrdemDeServicoUseCase(
                ordemGateway,
                clienteGateway,
                veiculoGateway,
                operadorGateway,
                servicoGateway,
                pecaGateway,
                estoqueGateway,
                notificacaoUseCase);
    }

    @Test
    void deveCriarOrdemComServicosEPecasNaAbertura() {
        var clienteId = UUID.randomUUID();
        var veiculoId = UUID.randomUUID();
        var operadorId = UUID.randomUUID();
        var servicoId = UUID.randomUUID();
        var pecaId = UUID.randomUUID();
        var estoque = new Estoque(UUID.randomUUID(), pecaId, 5);

        when(clienteGateway.buscarPorId(clienteId)).thenReturn(Optional.of(mock(Cliente.class)));
        when(veiculoGateway.buscarPorId(veiculoId)).thenReturn(Optional.of(mock(Veiculo.class)));
        when(operadorGateway.buscarPorId(operadorId)).thenReturn(Optional.of(mock(Operador.class)));
        when(servicoGateway.buscarPorId(servicoId)).thenReturn(Optional.of(
                new Servico(servicoId, "Troca de óleo", new BigDecimal("120.00"), true, null)));
        when(pecaGateway.buscarPorId(pecaId)).thenReturn(Optional.of(
                new Peca(pecaId, "Filtro de óleo", "Bosch", new BigDecimal("40.00"))));
        when(estoqueGateway.buscarEstoquePorPecaId(pecaId)).thenReturn(Optional.of(estoque));
        when(ordemGateway.salvar(any(OrdemDeServico.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = useCase.executar(new CriarOrdemDeServicoInput(
                clienteId,
                veiculoId,
                operadorId,
                List.of(servicoId),
                List.of(new CriarOrdemDeServicoInput.PecaInput(pecaId, 2))));

        assertEquals(1, resultado.getServicos().size());
        assertEquals(1, resultado.getPecas().size());
        assertEquals(new BigDecimal("200.00"), resultado.getValorTotalOs());
        assertEquals(3, estoque.getQuantidade());
        verify(estoqueGateway).salvar(estoque);
        verify(ordemGateway).salvar(any(OrdemDeServico.class));
        verify(notificacaoUseCase).executar(resultado);
    }

    @Test
    void naoDeveSalvarOrdemQuandoEstoqueForInsuficiente() {
        var clienteId = UUID.randomUUID();
        var veiculoId = UUID.randomUUID();
        var operadorId = UUID.randomUUID();
        var pecaId = UUID.randomUUID();

        when(clienteGateway.buscarPorId(clienteId)).thenReturn(Optional.of(mock(Cliente.class)));
        when(veiculoGateway.buscarPorId(veiculoId)).thenReturn(Optional.of(mock(Veiculo.class)));
        when(operadorGateway.buscarPorId(operadorId)).thenReturn(Optional.of(mock(Operador.class)));
        when(pecaGateway.buscarPorId(pecaId)).thenReturn(Optional.of(
                new Peca(pecaId, "Filtro de óleo", "Bosch", new BigDecimal("40.00"))));
        when(estoqueGateway.buscarEstoquePorPecaId(pecaId)).thenReturn(Optional.of(
                new Estoque(UUID.randomUUID(), pecaId, 1)));

        assertThrows(QuantidadeEstoqueException.class, () -> useCase.executar(
                new CriarOrdemDeServicoInput(
                        clienteId,
                        veiculoId,
                        operadorId,
                        List.of(),
                        List.of(new CriarOrdemDeServicoInput.PecaInput(pecaId, 2)))));

        verify(ordemGateway, never()).salvar(any(OrdemDeServico.class));
    }
}
