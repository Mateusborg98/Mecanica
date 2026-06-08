package br.com.techchallenge.mecanica.application.usecase.veiculo;

import br.com.techchallenge.mecanica.application.gateway.VeiculoGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DeletarVeiculoUseCase {

    private final VeiculoGateway gateway;

    public void executar(UUID id) {

        gateway.deletar(id);

    }
}
