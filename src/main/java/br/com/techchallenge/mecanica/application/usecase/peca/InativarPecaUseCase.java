package br.com.techchallenge.mecanica.application.usecase.peca;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.gateway.PecaGateway;
import br.com.techchallenge.mecanica.domain.exception.PecaNaoEncontradaException;
import br.com.techchallenge.mecanica.domain.peca.Peca;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InativarPecaUseCase {

    private final PecaGateway pecaGateway;

    public void executar(UUID id) {

        Peca peca = pecaGateway.buscarPorId(id)
                .orElseThrow(() -> new PecaNaoEncontradaException(
                        "Peça não encontrada"));

        peca.inativar();

        pecaGateway.salvar(peca);
    }
}