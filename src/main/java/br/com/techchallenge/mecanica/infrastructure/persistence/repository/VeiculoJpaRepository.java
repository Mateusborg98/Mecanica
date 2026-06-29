package br.com.techchallenge.mecanica.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techchallenge.mecanica.infrastructure.persistence.entity.VeiculoJpaEntity;

public interface VeiculoJpaRepository extends JpaRepository<VeiculoJpaEntity, UUID> {

    boolean existsByPlaca(String placa);

    Optional<VeiculoJpaEntity> findByPlaca(String placa);

    List<VeiculoJpaEntity> findByClienteJpaEntityId(UUID id);
}
