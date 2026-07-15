package br.com.techchallenge.mecanica.application.gateway;

import br.com.techchallenge.mecanica.domain.estoque.Estoque;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EstoqueGateway {

    Estoque salvar(Estoque estoque);

    Optional<Estoque> buscarEstoquePorPecaId(UUID pecaId);

    List<Estoque> listar();

    void deletar(UUID id);
}