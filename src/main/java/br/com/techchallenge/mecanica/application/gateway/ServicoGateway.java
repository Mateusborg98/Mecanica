package br.com.techchallenge.mecanica.application.gateway;

import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.domain.servico.Servico;

public interface ServicoGateway {

    Servico criar(Servico servico);

    Servico buscarPorId(UUID id);

    List<Servico> listar();

    Servico atualizar(UUID id, Servico servico);

    void deletar(UUID id);

}