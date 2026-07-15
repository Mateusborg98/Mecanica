package br.com.techchallenge.mecanica.application.usecase.estoque;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.gateway.EstoqueGateway;
import br.com.techchallenge.mecanica.domain.estoque.Estoque;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListarEstoquesUseCase {

    private final EstoqueGateway estoqueGateway;

    public List<Estoque> executar() {
        return estoqueGateway.listar();
    }
}
