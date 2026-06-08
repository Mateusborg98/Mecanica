package br.com.techchallenge.mecanica.application.usecase.cliente;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.gateway.ClienteGateway;
import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import br.com.techchallenge.mecanica.domain.cliente.valueobject.CpfCnpj;
import br.com.techchallenge.mecanica.domain.exception.CpfDuplicadoException;
import br.com.techchallenge.mecanica.presentation.cliente.CriarClienteRequest;

@Service
public class CriarClienteUseCase {

    private final ClienteGateway gateway;

    public CriarClienteUseCase(ClienteGateway gateway) {

        this.gateway = gateway;
    }

    public Cliente executar(CriarClienteRequest clienteRequest) {

        gateway.buscarPorCpfCnpj(clienteRequest.cpfCnpj()).ifPresent(cliente -> {
            throw new CpfDuplicadoException("CPF já cadastrado");
        });

        Cliente cliente = new Cliente(
                clienteRequest.nome(),
                new CpfCnpj(clienteRequest.cpfCnpj()),
                clienteRequest.contato(),
                clienteRequest.email());

        return gateway.salvar(cliente);
    }
}
