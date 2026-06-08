package br.com.techchallenge.mecanica.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import br.com.techchallenge.mecanica.infrastructure.persistence.entity.OperadorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperadorRepository extends JpaRepository<OperadorJpaEntity, UUID> {

    Optional<OperadorJpaEntity> findByMatricula(Integer matricula);
    
}
