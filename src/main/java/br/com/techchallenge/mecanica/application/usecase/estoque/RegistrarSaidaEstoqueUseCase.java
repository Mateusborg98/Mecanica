package br.com.techchallenge.mecanica.application.usecase.estoque;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.techchallenge.mecanica.application.dto.estoque.MovimentarEstoqueInput;
import br.com.techchallenge.mecanica.application.gateway.EstoqueGateway;
import br.com.techchallenge.mecanica.domain.estoque.Estoque;
import br.com.techchallenge.mecanica.domain.exception.EstoqueNaoEncontradoException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrarSaidaEstoqueUseCase {

    private final EstoqueGateway estoqueGateway;

    @Transactional
    public Estoque executar(MovimentarEstoqueInput input) {
        Estoque estoque = estoqueGateway.buscarEstoquePorPecaId(input.pecaId())
                .orElseThrow(() -> new EstoqueNaoEncontradoException("Estoque não encontrado"));
        estoque.registrarSaida(input.quantidade());
        return estoqueGateway.salvar(estoque);
    }
}
