package br.com.techchallenge.mecanica.presentation.veiculo;

import br.com.techchallenge.mecanica.domain.cliente.Cliente;

public record VeiculoResponse(
        String placa,
        String marca,
        String modelo,
        Integer ano,
        Cliente cliente
) {
}
