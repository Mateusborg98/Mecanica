package br.com.techchallenge.mecanica.application.usecase.peca;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.gateway.PecaGateway;
import br.com.techchallenge.mecanica.domain.peca.Peca;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListarPecasUseCase {

    private final PecaGateway pecaGateway;

    public List<Peca> executar() {
        return pecaGateway.listar();
    }
}