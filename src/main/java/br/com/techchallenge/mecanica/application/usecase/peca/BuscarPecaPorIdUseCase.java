package br.com.techchallenge.mecanica.application.usecase.peca;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.gateway.PecaGateway;
import br.com.techchallenge.mecanica.domain.exception.PecaNaoEncontradaException;
import br.com.techchallenge.mecanica.domain.peca.Peca;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuscarPecaPorIdUseCase {

    private final PecaGateway pecaGateway;

    public Peca executar(UUID id) {

        return pecaGateway.buscarPorId(id)
                .orElseThrow(() -> new PecaNaoEncontradaException(
                        "Peça não encontrada"));
    }
}