package br.com.techchallenge.mecanica.presentation.mapper;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.domain.veiculo.Veiculo;
import br.com.techchallenge.mecanica.presentation.dto.veiculo.VeiculoResponse;

@Component
public class VeiculoPresentationMapper {

    public VeiculoResponse toVeiculoResponse(Veiculo veiculo) {

        return new VeiculoResponse(
                veiculo.getPlaca().getValor(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getAno(),
                veiculo.getCliente());
    }

}
