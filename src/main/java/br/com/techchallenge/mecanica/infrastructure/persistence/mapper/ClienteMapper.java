package br.com.techchallenge.mecanica.infrastructure.persistence.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import br.com.techchallenge.mecanica.domain.cliente.valueobject.CpfCnpj;
import br.com.techchallenge.mecanica.domain.exception.CpfInvalidoException;
import br.com.techchallenge.mecanica.domain.veiculo.Veiculo;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ClienteJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.VeiculoJpaEntity;
import br.com.techchallenge.mecanica.presentation.cliente.ClienteResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ClienteMapper {

    private final VeiculoMapper veiculoMapper;

    public ClienteJpaEntity toJpaEntity(Cliente cliente) {
        List<VeiculoJpaEntity> veiculoJpaEntity = veiculoMapper.toListJpaEntity(cliente.getVeiculos());

        return ClienteJpaEntity.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .cpfCnpj(cliente.getCpfCnpj().getValor())
                .contato(cliente.getContato())
                .email(cliente.getEmail())
                .veiculoJpaEntities(veiculoJpaEntity)
                //.ordemDeServicos() PRECISA DESENVOLVER
                .build();
    }

    public Cliente toDomain(ClienteJpaEntity clienteJpaEntity) throws CpfInvalidoException {
        List<Veiculo> veiculos = veiculoMapper.toListVeiculo(clienteJpaEntity.getVeiculoJpaEntities());
        return Cliente.builder()
                .id(clienteJpaEntity.getId())
                .nome(clienteJpaEntity.getNome())
                .cpfCnpj(new CpfCnpj(clienteJpaEntity.getCpfCnpj()))
                .contato(clienteJpaEntity.getContato())
                .email(clienteJpaEntity.getEmail())
                .veiculos(veiculos)
                .build();
    }

    public ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getContato(),
                cliente.getEmail(),
                cliente.getVeiculos());
    }
}
