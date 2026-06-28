package br.com.techchallenge.mecanica.presentation.dto.veiculo;

import br.com.techchallenge.mecanica.presentation.dto.cliente.ClienteResponseResumo;

public record VeiculoResponse(
        String placa,
        String marca,
        String modelo,
        Integer ano,
        ClienteResponseResumo clienteResponseResumo
) {
}
