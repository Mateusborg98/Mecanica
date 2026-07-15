package br.com.techchallenge.mecanica.application.usecase.peca;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.dto.peca.CriarPecaInput;
import br.com.techchallenge.mecanica.application.gateway.PecaGateway;
import br.com.techchallenge.mecanica.domain.peca.Peca;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CriarPecaUseCase {

    private final PecaGateway pecaGateway;

    public Peca executar(
            CriarPecaInput input) {

        Peca peca = new Peca(
                input.nome(),
                input.marca(),
                input.preco());

        return pecaGateway.salvar(peca);
    }
}