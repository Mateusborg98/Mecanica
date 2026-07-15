package br.com.techchallenge.mecanica.infrastructure.persistence.gateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.gateway.PecaGateway;
import br.com.techchallenge.mecanica.domain.peca.Peca;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.PecaJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.mapper.PecaMapper;
import br.com.techchallenge.mecanica.infrastructure.persistence.repository.PecaJpaRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PecaGatewayImpl implements PecaGateway {

    private final PecaJpaRepository repository;
    private final PecaMapper mapper;

    @Override
    public Peca salvar(Peca peca) {

        PecaJpaEntity entity = mapper.toJpaEntity(peca);

        PecaJpaEntity salvo = repository.save(entity);

        return mapper.toDomain(salvo);
    }

    @Override
    public Optional<Peca> buscarPorId(UUID id) {

        return repository.findByIdAndAtivoTrue(id).map(mapper::toDomain);
    }

    @Override
    public List<Peca> listar() {

        return repository.findByAtivoTrue().stream().map(mapper::toDomain).toList();
    }

}
