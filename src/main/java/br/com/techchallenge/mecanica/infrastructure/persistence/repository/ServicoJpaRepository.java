package br.com.techchallenge.mecanica.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ServicoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoJpaRepository extends JpaRepository<ServicoJpaEntity, UUID> {
    Optional<ServicoJpaEntity> findByIdAndAtivoTrue(UUID id);
    List<ServicoJpaEntity> findByAtivoTrue();
}
