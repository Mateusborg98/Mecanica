package br.com.techchallenge.mecanica.application.usecase.estoque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.techchallenge.mecanica.application.dto.estoque.MovimentarEstoqueInput;
import br.com.techchallenge.mecanica.application.gateway.EstoqueGateway;
import br.com.techchallenge.mecanica.application.gateway.PecaGateway;
import br.com.techchallenge.mecanica.domain.estoque.Estoque;
import br.com.techchallenge.mecanica.domain.exception.EstoqueNaoEncontradoException;
import br.com.techchallenge.mecanica.domain.exception.PecaNaoEncontradaException;
import br.com.techchallenge.mecanica.domain.peca.Peca;

class EstoqueUseCasesTest {

    private EstoqueGateway estoqueGateway;
    private PecaGateway pecaGateway;

    @BeforeEach
    void preparar() {
        estoqueGateway = mock(EstoqueGateway.class);
        pecaGateway = mock(PecaGateway.class);
    }

    @Test
    void entradaDeveCriarEstoqueQuandoAindaNaoExiste() {
        var pecaId = UUID.randomUUID();
        var input = new MovimentarEstoqueInput(pecaId, 4);
        when(pecaGateway.buscarPorId(pecaId))
                .thenReturn(Optional.of(new Peca(pecaId, "Filtro", "Marca", BigDecimal.TEN)));
        when(estoqueGateway.buscarEstoquePorPecaId(pecaId)).thenReturn(Optional.empty());
        when(estoqueGateway.salvar(org.mockito.ArgumentMatchers.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = new RegistrarEntradaEstoqueUseCase(estoqueGateway, pecaGateway).executar(input);

        assertEquals(4, resultado.getQuantidade());
        verify(estoqueGateway).salvar(resultado);
    }

    @Test
    void entradaDeveRejeitarPecaInexistente() {
        var pecaId = UUID.randomUUID();
        when(pecaGateway.buscarPorId(pecaId)).thenReturn(Optional.empty());

        assertThrows(PecaNaoEncontradaException.class, () ->
                new RegistrarEntradaEstoqueUseCase(estoqueGateway, pecaGateway)
                        .executar(new MovimentarEstoqueInput(pecaId, 1)));
    }

    @Test
    void saidaDeveAtualizarSaldo() {
        var pecaId = UUID.randomUUID();
        var estoque = new Estoque(UUID.randomUUID(), pecaId, 5);
        when(estoqueGateway.buscarEstoquePorPecaId(pecaId)).thenReturn(Optional.of(estoque));
        when(estoqueGateway.salvar(estoque)).thenReturn(estoque);

        var resultado = new RegistrarSaidaEstoqueUseCase(estoqueGateway)
                .executar(new MovimentarEstoqueInput(pecaId, 2));

        assertEquals(3, resultado.getQuantidade());
    }

    @Test
    void saidaDeveRejeitarEstoqueInexistente() {
        var pecaId = UUID.randomUUID();
        when(estoqueGateway.buscarEstoquePorPecaId(pecaId)).thenReturn(Optional.empty());

        assertThrows(EstoqueNaoEncontradoException.class, () ->
                new RegistrarSaidaEstoqueUseCase(estoqueGateway)
                        .executar(new MovimentarEstoqueInput(pecaId, 1)));
    }

    @Test
    void deveBuscarEListarEstoques() {
        var pecaId = UUID.randomUUID();
        var estoque = new Estoque(UUID.randomUUID(), pecaId, 5);
        when(estoqueGateway.buscarEstoquePorPecaId(pecaId)).thenReturn(Optional.of(estoque));
        when(estoqueGateway.listar()).thenReturn(List.of(estoque));

        assertEquals(estoque, new BuscarEstoquePorPecaUseCase(estoqueGateway).executar(pecaId));
        assertEquals(List.of(estoque), new ListarEstoquesUseCase(estoqueGateway).executar());
    }
}
