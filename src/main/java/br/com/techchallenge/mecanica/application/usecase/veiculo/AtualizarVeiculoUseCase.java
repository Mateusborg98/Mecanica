package br.com.techchallenge.mecanica.application.usecase.veiculo;

import br.com.techchallenge.mecanica.application.gateway.VeiculoGateway;
import br.com.techchallenge.mecanica.domain.veiculo.Veiculo;
import br.com.techchallenge.mecanica.infrastructure.persistence.mapper.PlacaMapper;
import br.com.techchallenge.mecanica.presentation.veiculo.AtualizarVeiculoRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AtualizarVeiculoUseCase {

    private final VeiculoGateway gateway;
    private final PlacaMapper placaMapper;

    public Veiculo executar(UUID id, AtualizarVeiculoRequest atualizarVeiculoRequest) {

        Veiculo veiculo = gateway.buscarPorId(id).orElseThrow(()
                -> new RuntimeException("Veiculo não encontrado"));

        veiculo.atualizarDados(
                placaMapper.toDomain(atualizarVeiculoRequest.placa()),
                atualizarVeiculoRequest.marca(),
                atualizarVeiculoRequest.modelo(),
                atualizarVeiculoRequest.ano());

        return gateway.salvar(veiculo);
    }
}
