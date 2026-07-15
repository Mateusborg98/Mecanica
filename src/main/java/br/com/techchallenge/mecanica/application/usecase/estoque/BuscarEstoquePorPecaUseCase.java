package br.com.techchallenge.mecanica.application.usecase.estoque;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.gateway.EstoqueGateway;
import br.com.techchallenge.mecanica.domain.estoque.Estoque;
import br.com.techchallenge.mecanica.domain.exception.EstoqueNaoEncontradoException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuscarEstoquePorPecaUseCase {

    private final EstoqueGateway estoqueGateway;

    public Estoque executar(UUID pecaId) {
        return estoqueGateway.buscarEstoquePorPecaId(pecaId)
                .orElseThrow(() -> new EstoqueNaoEncontradoException("Estoque não encontrado"));
    }
}
