package br.com.techchallenge.mecanica.infrastructure.persistence.gateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.gateway.ServicoGateway;
import br.com.techchallenge.mecanica.domain.servico.Servico;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ServicoJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.mapper.ServicoMapper;
import br.com.techchallenge.mecanica.infrastructure.persistence.repository.ServicoJpaRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ServicoGatewayImpl implements ServicoGateway {

    private final ServicoJpaRepository repository;
    private final ServicoMapper mapper;

    @Override
    public Servico salvar(Servico servico) {

        ServicoJpaEntity entity = mapper.toJpaEntity(servico);

        ServicoJpaEntity salvo = repository.save(entity);

        return mapper.toDomain(salvo);
    }

    @Override
    public Optional<Servico> buscarPorId(UUID id) {

        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Servico> listar() {

        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deletar(UUID id) {

        repository.deleteById(id);
    }

}