package br.com.techchallenge.mecanica.presentation.cliente;

import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.domain.veiculo.Veiculo;

public record ClienteResponse(
        UUID id,
        String nome,
        String contato,
        String email,
        List<Veiculo> veiculos
) {
}
