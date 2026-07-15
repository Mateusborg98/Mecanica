package br.com.techchallenge.mecanica.infrastructure.persistence.gateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.application.gateway.ServicoGateway;
import br.com.techchallenge.mecanica.domain.servico.Servico;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ServicoJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.mapper.ServicoMapper;
import br.com.techchallenge.mecanica.infrastructure.persistence.repository.ServicoJpaRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ServicoGatewayImpl implements ServicoGateway {

    private final ServicoJpaRepository repository;
    private final ServicoMapper servicoMapper;

    @Override
    public Servico salvar(Servico servico) {
        ServicoJpaEntity servicoJpaEntity = servicoMapper.toEntity(servico);

        ServicoJpaEntity salvo = repository.save(servicoJpaEntity);

        return servicoMapper.toDomain(salvo);
    }

    @Override
    public Optional<Servico> buscarPorId(UUID id) {
        return repository.findByIdAndAtivoTrue(id).map(servicoMapper::toDomain);
    }

    @Override
    public List<Servico> listar() {
        return repository.findByAtivoTrue()
                .stream()
                .map(servicoMapper::toDomain)
                .toList();
    }

}
