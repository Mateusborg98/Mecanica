package br.com.techchallenge.mecanica.application.usecase.cliente;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.gateway.ClienteGateway;
import br.com.techchallenge.mecanica.application.gateway.VeiculoGateway;
import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import br.com.techchallenge.mecanica.domain.veiculo.Veiculo;
import br.com.techchallenge.mecanica.presentation.cliente.AtualizarClienteRequest;
import br.com.techchallenge.mecanica.presentation.cliente.ClienteResponse;
import br.com.techchallenge.mecanica.presentation.mapper.ClienteMapperPresentation;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AtualizarClienteUseCase {

    private final ClienteGateway gateway;
    private final VeiculoGateway veiculoGateway;
    private final ClienteMapperPresentation mapperPresentation;

    public ClienteResponse executar(UUID id, AtualizarClienteRequest atualizarClienteRequest) {

        Cliente cliente = gateway.buscarPorId(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        cliente.atualizarDados(
                atualizarClienteRequest.nome(),
                atualizarClienteRequest.contato(),
                atualizarClienteRequest.email());

        List<Veiculo> veiculos = veiculoGateway.buscarPorClienteId(id);

        Cliente salvo = gateway.salvar(cliente);

        return mapperPresentation.toResponse(salvo, veiculos);
    }
}
