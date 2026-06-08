package br.com.techchallenge.mecanica.application.usecase.veiculo;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.gateway.VeiculoGateway;
import br.com.techchallenge.mecanica.domain.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.domain.veiculo.Veiculo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BuscarVeiculoPorIdUseCase {

    private final VeiculoGateway gateway;

    public Veiculo executar(UUID id) {

        return gateway.buscarPorId(id).orElseThrow(() ->
                new RegraNegocioException("Veiculo não encontrado!"));
    }
}
