package br.com.techchallenge.mecanica.application.usecase.veiculo;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.gateway.ClienteGateway;
import br.com.techchallenge.mecanica.application.gateway.VeiculoGateway;
import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import br.com.techchallenge.mecanica.domain.exception.ClienteNaoEncontradoException;
import br.com.techchallenge.mecanica.domain.exception.VeiculoDuplicadoException;
import br.com.techchallenge.mecanica.domain.veiculo.Veiculo;
import br.com.techchallenge.mecanica.infrastructure.persistence.mapper.PlacaMapper;
import br.com.techchallenge.mecanica.presentation.veiculo.CriarVeiculoRequest;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CriarVeiculoUseCase {

    private final VeiculoGateway gateway;
    private final ClienteGateway clienteGateway;
    private final PlacaMapper placaMapper;

    public Veiculo executar(CriarVeiculoRequest veiculoRequest) {


        Cliente cliente = clienteGateway.buscarPorId(veiculoRequest.clienteId())
                .orElseThrow(() ->
                        new ClienteNaoEncontradoException("Cliente não encontrado " + veiculoRequest.clienteId().toString()));

        gateway.buscarPorPlaca(veiculoRequest.placa()).ifPresent(veiculo -> {
            throw new VeiculoDuplicadoException("Veiculo já registrado");
        });

        Veiculo veiculo = new Veiculo(
                placaMapper.toDomain(veiculoRequest.placa()),
                veiculoRequest.marca(),
                veiculoRequest.modelo(),
                veiculoRequest.ano(),
                cliente
        );

        return gateway.salvar(veiculo);
    }
}
