package br.com.techchallenge.mecanica.application.usecase.cliente;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.gateway.ClienteGateway;
import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import br.com.techchallenge.mecanica.domain.exception.ClienteNaoEncontradoException;

@Service
public class BuscarClientePorCpfCnpjUseCase {

    private final ClienteGateway gateway;

    public BuscarClientePorCpfCnpjUseCase(ClienteGateway gateway) {

        this.gateway = gateway;
    }

    public Cliente executar(String cpfCnpj) {

        return gateway.buscarPorCpfCnpj(cpfCnpj).orElseThrow(() ->
                new ClienteNaoEncontradoException("Cliente não encontrado!"));

    }
}
