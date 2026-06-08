package br.com.techchallenge.mecanica.application.usecase.cliente;

import br.com.techchallenge.mecanica.application.gateway.ClienteGateway;
import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import br.com.techchallenge.mecanica.presentation.cliente.AtualizarClienteRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AtualizarClienteUseCase {

    private final ClienteGateway gateway;

    public Cliente executar(UUID id, AtualizarClienteRequest atualizarClienteRequest) {

        Cliente cliente = gateway.buscarPorId(id).orElseThrow(()
                -> new RuntimeException("Cliente não encontrado"));

        cliente.atualizarDados(
                atualizarClienteRequest.nome(),
                atualizarClienteRequest.contato(),
                atualizarClienteRequest.email());

        return gateway.salvar(cliente);
    }
}
