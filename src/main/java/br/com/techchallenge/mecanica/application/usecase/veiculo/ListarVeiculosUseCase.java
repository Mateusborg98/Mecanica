package br.com.techchallenge.mecanica.application.usecase.veiculo;

import br.com.techchallenge.mecanica.application.gateway.VeiculoGateway;
import br.com.techchallenge.mecanica.domain.veiculo.Veiculo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ListarVeiculosUseCase {

    private final VeiculoGateway gateway;

    public List<Veiculo> executar() {

        return gateway.listar();

    }
}
