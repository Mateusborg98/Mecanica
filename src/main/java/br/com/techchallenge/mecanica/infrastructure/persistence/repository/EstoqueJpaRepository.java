package br.com.techchallenge.mecanica.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techchallenge.mecanica.infrastructure.persistence.entity.EstoqueJpaEntity;

public interface EstoqueJpaRepository extends JpaRepository<EstoqueJpaEntity, UUID> {

    Optional<EstoqueJpaEntity> findByPecaJpaEntityId(UUID pecaId);

}
