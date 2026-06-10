package br.com.techchallenge.mecanica.infrastructure.persistence.gateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.application.gateway.OperadorGateway;
import br.com.techchallenge.mecanica.domain.operador.Operador;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.OperadorJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.mapper.OperadorMapper;
import br.com.techchallenge.mecanica.infrastructure.persistence.repository.OperadorJpaRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OperadorGatewayImpl implements OperadorGateway {

    private final OperadorJpaRepository repository;
    private final OperadorMapper mapper;

    @Override
    public Operador salvar(Operador operador) {

        OperadorJpaEntity entity = mapper.toJpaEntity(operador);

        OperadorJpaEntity salvo = repository.save(entity);

        return mapper.toDomain(salvo);
    }

    @Override
    public Optional<Operador> buscarPorId(UUID id) {

        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Operador> buscarPorMatricula(Integer matricula) {

        return repository.findByMatricula(matricula).map(mapper::toDomain);
    }

    @Override
    public List<Operador> listar() {

        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deletar(UUID id) {

        repository.deleteById(id);
    }
}
