package br.com.techchallenge.mecanica.presentation.cliente;

import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.presentation.veiculo.VeiculoResponse;

public record ClienteResponse(
        UUID id,
        String nome,
        String contato,
        String email,
        List<VeiculoResponse> veiculos
) {
}
