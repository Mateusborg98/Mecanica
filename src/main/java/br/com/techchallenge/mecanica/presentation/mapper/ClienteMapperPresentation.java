package br.com.techchallenge.mecanica.presentation.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import br.com.techchallenge.mecanica.domain.veiculo.Veiculo;
import br.com.techchallenge.mecanica.presentation.cliente.ClienteResponse;
import br.com.techchallenge.mecanica.presentation.veiculo.VeiculoResponse;

@Component
public class ClienteMapperPresentation {

    public ClienteResponse toResponse(Cliente cliente, List<Veiculo> veiculos) {

        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getContato(),
                cliente.getEmail(),
                veiculos.stream()
                        .map(this::toVeiculoResponse)
                        .toList());
    }

    private VeiculoResponse toVeiculoResponse(Veiculo veiculo) {

        return new VeiculoResponse(
                veiculo.getPlaca().getValor(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getAno(),
                veiculo.getCliente());
    }
}
