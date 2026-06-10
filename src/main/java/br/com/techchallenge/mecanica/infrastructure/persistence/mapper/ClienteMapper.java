package br.com.techchallenge.mecanica.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import br.com.techchallenge.mecanica.domain.cliente.valueobject.CpfCnpj;
import br.com.techchallenge.mecanica.domain.exception.CpfInvalidoException;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ClienteJpaEntity;

@Component
public class ClienteMapper {

    public ClienteJpaEntity toJpaEntity(Cliente cliente) {

        return ClienteJpaEntity.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .cpfCnpj(cliente.getCpfCnpj().getValor())
                .contato(cliente.getContato())
                .email(cliente.getEmail())
                .build();
    }

    public Cliente toDomain(ClienteJpaEntity clienteJpaEntity) throws CpfInvalidoException {

        return Cliente.builder()
                .id(clienteJpaEntity.getId())
                .nome(clienteJpaEntity.getNome())
                .cpfCnpj(new CpfCnpj(clienteJpaEntity.getCpfCnpj()))
                .contato(clienteJpaEntity.getContato())
                .email(clienteJpaEntity.getEmail())
                .build();
    }
}
