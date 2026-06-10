package br.com.techchallenge.mecanica.infrastructure.persistence.gateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.application.gateway.EstoqueGateway;
import br.com.techchallenge.mecanica.domain.estoque.Estoque;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.EstoqueJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.mapper.EstoqueMapper;
import br.com.techchallenge.mecanica.infrastructure.persistence.repository.EstoqueJpaRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EstoqueGatewayImpl implements EstoqueGateway {

    private final EstoqueJpaRepository repository;
    private final EstoqueMapper mapper;

    @Override
    public Estoque salvar(Estoque estoque) {

        EstoqueJpaEntity entity = mapper.toJpaEntity(estoque);

        EstoqueJpaEntity salvo = repository.save(entity);

        return mapper.toDomain(salvo);
    }

    @Override
    public Optional<Estoque> buscarEstoquePorPecaId(UUID id) {

        return repository.findByPecaJpaEntityId(id).map(mapper::toDomain);
    }

    @Override
    public List<Estoque> listar() {

        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deletar(UUID id) {

        repository.deleteById(id);
    }
}