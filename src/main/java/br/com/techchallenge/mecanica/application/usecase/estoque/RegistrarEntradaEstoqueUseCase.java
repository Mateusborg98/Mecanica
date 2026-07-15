package br.com.techchallenge.mecanica.application.usecase.estoque;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.techchallenge.mecanica.application.dto.estoque.MovimentarEstoqueInput;
import br.com.techchallenge.mecanica.application.gateway.EstoqueGateway;
import br.com.techchallenge.mecanica.application.gateway.PecaGateway;
import br.com.techchallenge.mecanica.domain.estoque.Estoque;
import br.com.techchallenge.mecanica.domain.exception.PecaNaoEncontradaException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrarEntradaEstoqueUseCase {

    private final EstoqueGateway estoqueGateway;
    private final PecaGateway pecaGateway;

    @Transactional
    public Estoque executar(MovimentarEstoqueInput input) {
        pecaGateway.buscarPorId(input.pecaId())
                .orElseThrow(() -> new PecaNaoEncontradaException("Peça não encontrada"));

        Estoque estoque = estoqueGateway.buscarEstoquePorPecaId(input.pecaId())
                .orElseGet(() -> new Estoque(input.pecaId(), 0));
        estoque.registrarEntrada(input.quantidade());
        return estoqueGateway.salvar(estoque);
    }
}
