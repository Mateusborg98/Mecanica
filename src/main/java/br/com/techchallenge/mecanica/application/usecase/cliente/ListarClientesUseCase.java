package br.com.techchallenge.mecanica.application.usecase.cliente;

import br.com.techchallenge.mecanica.application.gateway.ClienteGateway;
import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarClientesUseCase {

    private final ClienteGateway gateway;

    public ListarClientesUseCase(ClienteGateway gateway) {

        this.gateway = gateway;
    }

    public List<Cliente> executar() {

        return gateway.listar();

    }
}
