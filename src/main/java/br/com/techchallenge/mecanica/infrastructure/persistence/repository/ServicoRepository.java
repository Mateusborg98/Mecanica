package br.com.techchallenge.mecanica.infrastructure.persistence.repository;

import java.util.UUID;

import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ServicoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<ServicoJpaEntity, UUID> {

}
