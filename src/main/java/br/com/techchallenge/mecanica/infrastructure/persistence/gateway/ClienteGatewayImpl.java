package br.com.techchallenge.mecanica.infrastructure.persistence.gateway;

import br.com.techchallenge.mecanica.application.gateway.ClienteGateway;
import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import br.com.techchallenge.mecanica.domain.exception.CpfInvalidoException;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ClienteJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.mapper.ClienteMapper;
import br.com.techchallenge.mecanica.infrastructure.persistence.repository.ClienteJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ClienteGatewayImpl implements ClienteGateway {

    private final ClienteJpaRepository repository;
    private final ClienteMapper mapper;

    @Override
    public Cliente salvar(Cliente cliente) throws CpfInvalidoException {

        ClienteJpaEntity entity = mapper.toJpaEntity(cliente);

        ClienteJpaEntity salvo = repository.save(entity);

        return mapper.toDomain(salvo);
    }

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorCpfCnpj(String cpfCnpj) {
        return repository.findByCpfCnpj(cpfCnpj).map(mapper::toDomain);
    }

    @Override
    public List<Cliente> listar() {

        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deletar(UUID id) {

        repository.deleteById(id);
    }
}
