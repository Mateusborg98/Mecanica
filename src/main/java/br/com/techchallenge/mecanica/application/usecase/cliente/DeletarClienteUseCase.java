package br.com.techchallenge.mecanica.application.usecase.cliente;

import br.com.techchallenge.mecanica.application.gateway.ClienteGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeletarClienteUseCase {

    private final ClienteGateway gateway;

    public DeletarClienteUseCase(ClienteGateway gateway) {

        this.gateway = gateway;
    }

    public void executar(UUID id) {

        gateway.deletar(id);

    }
}
