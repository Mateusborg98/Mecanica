package br.com.techchallenge.mecanica.application.usecase.peca;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.dto.peca.AtualizarPecaInput;
import br.com.techchallenge.mecanica.application.gateway.PecaGateway;
import br.com.techchallenge.mecanica.domain.exception.PecaNaoEncontradaException;
import br.com.techchallenge.mecanica.domain.peca.Peca;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AtualizarPecaUseCase {

    private final PecaGateway pecaGateway;

    public Peca executar(
            UUID id,
            AtualizarPecaInput input) {

        Peca peca = pecaGateway.buscarPorId(id)
                .orElseThrow(() -> new PecaNaoEncontradaException(
                        "Peça não encontrada"));

        peca.atualizarDados(
                input.nome(),
                input.marca(),
                input.preco());

        return pecaGateway.salvar(peca);
    }
}