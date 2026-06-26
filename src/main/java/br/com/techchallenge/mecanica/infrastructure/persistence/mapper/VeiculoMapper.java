package br.com.techchallenge.mecanica.infrastructure.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import br.com.techchallenge.mecanica.domain.exception.CpfInvalidoException;
import br.com.techchallenge.mecanica.domain.exception.PlacaInvalidaException;
import br.com.techchallenge.mecanica.domain.veiculo.Veiculo;
import br.com.techchallenge.mecanica.domain.veiculo.valueObject.Placa;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ClienteJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.VeiculoJpaEntity;
import br.com.techchallenge.mecanica.presentation.dto.veiculo.VeiculoResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VeiculoMapper {

    private final ClienteMapper clienteMapper;

    public VeiculoJpaEntity toJpaEntity(Veiculo veiculo) {
        ClienteJpaEntity clienteJpaEntity = clienteMapper.toJpaEntity(veiculo.getCliente());

        return VeiculoJpaEntity.builder()
                .id(veiculo.getId())
                .placa(veiculo.getPlaca().getValor())
                .marca(veiculo.getMarca())
                .modelo(veiculo.getModelo())
                .ano(veiculo.getAno())
                .clienteJpaEntity(clienteJpaEntity)
                .build();
    }

    public List<VeiculoJpaEntity> toListJpaEntity(List<Veiculo> veiculos) {

        List<VeiculoJpaEntity> veiculoJpaEntities = new ArrayList<>();

        for (Veiculo veiculo : veiculos) {
            ClienteJpaEntity clienteJpaEntity = clienteMapper.toJpaEntity(veiculo.getCliente());

            veiculoJpaEntities.add(VeiculoJpaEntity.builder()
                    .id(veiculo.getId())
                    .placa(veiculo.getPlaca().getValor())
                    .marca(veiculo.getMarca())
                    .modelo(veiculo.getModelo())
                    .ano(veiculo.getAno())
                    .clienteJpaEntity(clienteJpaEntity)
                    .build());

        }
        return veiculoJpaEntities;
    }

    public Veiculo toDomain(VeiculoJpaEntity veiculoJpaEntity) throws PlacaInvalidaException, CpfInvalidoException {
        Cliente cliente = clienteMapper.toDomain(veiculoJpaEntity.getClienteJpaEntity());
        return Veiculo.builder()
                .id(veiculoJpaEntity.getId())
                .placa(new Placa(veiculoJpaEntity.getPlaca()))
                .marca(veiculoJpaEntity.getMarca())
                .modelo(veiculoJpaEntity.getModelo())
                .ano(veiculoJpaEntity.getAno())
                .cliente(cliente)
                .build();
    }

    public List<Veiculo> toListVeiculo(List<VeiculoJpaEntity> veiculosJpaEntities) {

        List<Veiculo> veiculosList = new ArrayList<>();

        for (VeiculoJpaEntity veiculo : veiculosJpaEntities) {
            Cliente cliente = clienteMapper.toDomain(veiculo.getClienteJpaEntity());

            veiculosList.add(Veiculo.builder()
                    .id(veiculo.getId())
                    .placa(new Placa(veiculo.getPlaca()))
                    .marca(veiculo.getMarca())
                    .modelo(veiculo.getModelo())
                    .ano(veiculo.getAno())
                    .cliente(cliente)
                    .build());
        }
        return veiculosList;
    }

    public VeiculoResponse toResponse(Veiculo veiculo) {
        return new VeiculoResponse(
                veiculo.getPlaca().getValor(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getAno(),
                veiculo.getCliente());
    }
}
