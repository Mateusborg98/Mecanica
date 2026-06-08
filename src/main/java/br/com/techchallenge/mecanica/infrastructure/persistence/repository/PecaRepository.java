package br.com.techchallenge.mecanica.infrastructure.persistence.repository;

import java.util.UUID;

import br.com.techchallenge.mecanica.infrastructure.persistence.entity.PecaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PecaRepository extends JpaRepository<PecaJpaEntity, UUID> {
    
}
