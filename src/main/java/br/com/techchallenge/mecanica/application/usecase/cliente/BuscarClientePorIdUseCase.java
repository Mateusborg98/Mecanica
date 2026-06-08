package br.com.techchallenge.mecanica.application.usecase.cliente;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.gateway.ClienteGateway;
import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import br.com.techchallenge.mecanica.domain.exception.ClienteNaoEncontradoException;

@Service
public class BuscarClientePorIdUseCase {

    private final ClienteGateway gateway;

    public BuscarClientePorIdUseCase(ClienteGateway gateway) {

        this.gateway = gateway;
    }

    public Cliente executar(UUID id) {

        return gateway.buscarPorId(id).orElseThrow(() ->
                new ClienteNaoEncontradoException("Cliente não encontrado!"));

    }
}
