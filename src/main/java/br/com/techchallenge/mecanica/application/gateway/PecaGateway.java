package br.com.techchallenge.mecanica.application.gateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.techchallenge.mecanica.domain.peca.Peca;

public interface PecaGateway {

    Peca salvar(Peca peca);

    Optional<Peca> buscarPorId(UUID id);

    List<Peca> listar();

}