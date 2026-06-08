package br.com.techchallenge.mecanica.application.gateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.techchallenge.mecanica.domain.peca.Peca;

public interface PecaGateway {

    Peca criar(Peca peca);

    Optional<Peca> buscarPorId(UUID id);

    List<Peca> listar();

    Peca atualizar(UUID id, Peca peca);

    void deletar(UUID id);

    void registrarSaidaEstoque(UUID pecaId, int quantidade);

    void registrarEntradaEstoque(UUID pecaId, int quantidade);
}