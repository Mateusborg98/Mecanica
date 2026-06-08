package br.com.techchallenge.mecanica.application.usecase.veiculo;

import br.com.techchallenge.mecanica.application.gateway.ClienteGateway;
import br.com.techchallenge.mecanica.application.gateway.VeiculoGateway;
import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import br.com.techchallenge.mecanica.domain.veiculo.Veiculo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AtualizarIdClienteDoVeiculoUseCase {

    private final VeiculoGateway veiculoGateway;
    private final ClienteGateway clienteGateway;

    public Veiculo executar(UUID idVeiculo, UUID idCliente) {

        Veiculo veiculo = veiculoGateway.buscarPorId(idVeiculo).orElseThrow(()
                -> new RuntimeException("Veiculo não encontrado"));

        Cliente cliente = clienteGateway.buscarPorId(idCliente).orElseThrow(()
                -> new RuntimeException("Cliente não encontrado"));

        veiculo.atualizarIdCliente(cliente);

        return veiculoGateway.salvar(veiculo);
    }
}
