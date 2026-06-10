package br.com.techchallenge.mecanica.infrastructure.persistence.gateway;

import br.com.techchallenge.mecanica.application.gateway.VeiculoGateway;
import br.com.techchallenge.mecanica.domain.exception.CpfInvalidoException;
import br.com.techchallenge.mecanica.domain.exception.PlacaInvalidaException;
import br.com.techchallenge.mecanica.domain.veiculo.Veiculo;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.VeiculoJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.mapper.VeiculoMapper;
import br.com.techchallenge.mecanica.infrastructure.persistence.repository.VeiculoJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class VeiculoGatewayImpl implements VeiculoGateway {

    private final VeiculoJpaRepository repository;
    private final VeiculoMapper mapper;

    @Override
    public Veiculo salvar(Veiculo veiculo) throws PlacaInvalidaException, CpfInvalidoException {

        VeiculoJpaEntity entity = mapper.toJpaEntity(veiculo);

        VeiculoJpaEntity salvo = repository.save(entity);

        return mapper.toDomain(salvo);
    }

    @Override
    public Optional<Veiculo> buscarPorId(UUID id) {

        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Veiculo> buscarPorPlaca(String placa) {

        return repository.findByPlaca(placa).map(mapper::toDomain);
    }

    @Override
    public List<Veiculo> listar() {

        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deletar(UUID id) {

        repository.deleteById(id);
    }

    @Override
    public List<Veiculo> buscarPorClienteId(UUID id) {
        return repository.findByClienteJpaEntityId(id)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}