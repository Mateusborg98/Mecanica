package br.com.techchallenge.mecanica.application.gateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.techchallenge.mecanica.domain.servico.Servico;

public interface ServicoGateway {

    Servico salvar(Servico servico);

    Optional<Servico> buscarPorId(UUID id);

    List<Servico> listar();

    void deletar(UUID id);

}